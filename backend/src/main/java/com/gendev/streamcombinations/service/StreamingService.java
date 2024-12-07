package com.gendev.streamcombinations.service;
import com.gendev.streamcombinations.indexer.*;
import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.helper.GameOffer;
import com.gendev.streamcombinations.model.main.StreamingOffer;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class StreamingService {

    private final GameFetch gameFetch;
    private final StreamingOfferFetch streamingOfferFetch;
    private final StreamingPackageFetch streamingPackageFetch;


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

    public List<StreamingPackage> findCheapestCombination(Set<Game> requiredGames){
        // ToDo
        return CheapestComb.findCheapestCombination(requiredGames, buildPackageToGameOffers(requiredGames));
    }

    public List<StreamingPackage> findLeastServicesCombination(Set<Game> requiredGames){

        return LeastServicesAlgorithm.leastServicesAlgorithm(requiredGames, buildPackageToGameOffers(requiredGames));

    }
}
