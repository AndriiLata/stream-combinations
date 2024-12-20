package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.indexer.*;
import com.gendev.streamcombinations.model.helper.GameOffer;
import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.main.StreamingOffer;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import com.gendev.streamcombinations.model.response.SearchResult;
import com.gendev.streamcombinations.util.DateUtils;
import com.gendev.streamcombinations.util.SubscriptionCostUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class StreamingService {

    private final GameFetch gameFetch;
    private final StreamingOfferFetch streamingOfferFetch;
    private final StreamingPackageFetch streamingPackageFetch;
    @Autowired
    private UserService userService;

    @Autowired
    public StreamingService(GameFetch gameFetch, StreamingOfferFetch streamingOfferFetch, StreamingPackageFetch streamingPackageFetch) {
        this.gameFetch = gameFetch;
        this.streamingOfferFetch = streamingOfferFetch;
        this.streamingPackageFetch = streamingPackageFetch;
    }

    public Set<Game> getRequiredGames(Set<String> teams, Set<String> tournaments, LocalDateTime startDate, LocalDateTime endDate) {
        Set<Game> requiredGames = new HashSet<>();

        if(teams==null && tournaments==null){
            requiredGames.addAll(gameFetch.getAllGames());
        }

        if(teams != null) {
            for (String team : teams) {
                requiredGames.addAll(gameFetch.getGamesByTeam(team));
            }
        }

        if(tournaments != null) {
            for (String tournament : tournaments) {
                requiredGames.addAll(gameFetch.getGamesByTournament(tournament));
            }
        }

        if (startDate != null && endDate != null) {
            requiredGames.removeIf(game -> game.getStarts_at().isBefore(startDate) || game.getStarts_at().isAfter(endDate));
        }

        return requiredGames;
    }

    public List<StreamingPackage> getPackagesByIds(List<Integer> ids) {
        List<StreamingPackage> result = new ArrayList<>();
        for (Integer id : ids) {
            StreamingPackage sp = streamingPackageFetch.getPackageById(id);
            if (sp != null) {
                result.add(sp);
            }
        }
        return result;
    }


    public Map<StreamingPackage, Set<GameOffer>> buildPackageToGameOffers(Set<Game> requiredGames) {
        Map<StreamingPackage, Set<GameOffer>> packageToGameOffers = new HashMap<>();

        for (Game game : requiredGames) {
            List<StreamingOffer> offers = streamingOfferFetch.getOffersByGameId(game.getId());
            for (StreamingOffer offer : offers) {
                StreamingPackage sp = streamingPackageFetch.getPackageById(offer.getStreaming_package_id());
                if (sp != null) {
                    GameOffer gameOffer = new GameOffer(game, offer.isLive(), offer.isHighlights());
                    packageToGameOffers.computeIfAbsent(sp, k -> new HashSet<>()).add(gameOffer);
                }
            }
        }

        return packageToGameOffers;
    }

    public List<StreamingPackage> findCheapestCombination(Set<Game> requiredGames, int monthsDifference) {
        Map<StreamingPackage, Set<GameOffer>> packageToGameOffers = buildPackageToGameOffers(requiredGames);
        List<StreamingPackage> allPackages = new ArrayList<>(packageToGameOffers.keySet());
        int[] costs = new int[allPackages.size()];

        Set<Integer> boughtPackages = userService.getUser().getBoughtPackageIds();

        for (int i = 0; i < allPackages.size(); i++) {
            StreamingPackage sp = allPackages.get(i);
            // If user already bought it, cost = 0
            if (boughtPackages.contains(sp.getId())) {
                costs[i] = 0;
            } else {
                costs[i] = SubscriptionCostUtil.calculateTotalCost(sp, monthsDifference);
            }
        }

        return BestPackageCombination.findCheapestCombination(requiredGames, packageToGameOffers, allPackages, costs);
    }

    public List<StreamingPackage> rankOtherPackages(Set<Game> requiredGames, Map<StreamingPackage, Set<GameOffer>> packageToGameOffers, List<StreamingPackage> streamingPackages, int monthsDifference){
        return RankOtherPackages.rankOtherPackages(requiredGames, packageToGameOffers, streamingPackages, monthsDifference);
    }

    @Cacheable(
            value = "searchResults",
            key = "T(com.gendev.streamcombinations.service.StreamingService).buildCacheKey(#teams, #tournaments, #startDate, #endDate, @userService.getUser().boughtPackageIds)"
    )
    public SearchResult getBestPackagesResult(Set<String> teams, Set<String> tournaments, String startDate, String endDate) {
        LocalDateTime start = DateUtils.parse(startDate);
        LocalDateTime end = DateUtils.parse(endDate);

        Set<Game> games = getRequiredGames(teams, tournaments, start, end);

        int monthsDifference = -1;
        if (start != null && end != null) {
            monthsDifference = DateUtils.monthsBetween(start, end);
            if (monthsDifference < 0) {
                monthsDifference = -1;
            }
        }

        List<StreamingPackage> streamingPackages = findCheapestCombination(games, monthsDifference);
        Map<StreamingPackage, Set<GameOffer>> packageToGameOffers = buildPackageToGameOffers(games);
        List<StreamingPackage> otherPackages = rankOtherPackages(games, packageToGameOffers, streamingPackages, monthsDifference);

        for (StreamingPackage pkg : streamingPackages) {
            packageToGameOffers.putIfAbsent(pkg, Collections.emptySet());
        }

        for (StreamingPackage pkg : otherPackages) {
            packageToGameOffers.putIfAbsent(pkg, Collections.emptySet());
        }

        return new SearchResult(games, streamingPackages, packageToGameOffers, otherPackages);
    }


    public static String buildCacheKey(Set<String> teams, Set<String> tournaments, String startDate, String endDate, Set<Integer> boughtPackageIds) {
        // Normalize sets: convert null to empty, sort them
        String teamsKey = (teams == null || teams.isEmpty()) ? "" : teams.stream().sorted().collect(Collectors.joining(","));
        String tournamentsKey = (tournaments == null || tournaments.isEmpty()) ? "" : tournaments.stream().sorted().collect(Collectors.joining(","));
        String startKey = (startDate == null) ? "" : startDate;
        String endKey = (endDate == null) ? "" : endDate;

        List<Integer> sortedBoughtIds = new ArrayList<>(boughtPackageIds);
        Collections.sort(sortedBoughtIds);
        String userPackagesKey = sortedBoughtIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return teamsKey + "|" + tournamentsKey + "|" + startKey + "|" + endKey + "|" + userPackagesKey;
    }

}
