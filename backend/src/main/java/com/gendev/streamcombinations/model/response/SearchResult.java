package com.gendev.streamcombinations.model.response;

import com.gendev.streamcombinations.model.helper.IDoffer;
import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.helper.GameOffer;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import lombok.Getter;

import java.util.*;

@Getter
public class SearchResult {

    private final Map<String, Map<Integer, Game>> gamesByTournament;
    private final List<StreamingPackage> streamingPackages;
    private final Map<Integer, Map<Integer, IDoffer>> offersToPackageID;
    private final List<StreamingPackage> otherPackages;

    public SearchResult(Set<Game> games,
                        List<StreamingPackage> streamingPackages,
                        Map<StreamingPackage, Set<GameOffer>> packageToGameOffers,
                        List<StreamingPackage> otherPackages) {
        this.gamesByTournament = getGamesByTournament(games);
        this.streamingPackages = streamingPackages;
        this.otherPackages = otherPackages;
        this.offersToPackageID = getOffersToPackageID(streamingPackages, otherPackages, packageToGameOffers);
    }

    private Map<String, Map<Integer, Game>> getGamesByTournament(Set<Game> games) {
        Map<String, Map<Integer, Game>> gamesByT = new HashMap<>();
        for (Game game : games) {
            gamesByT
                    .computeIfAbsent(game.getTournament_name(), k -> new HashMap<>())
                    .put(game.getId(), game);
        }
        return gamesByT;
    }

    private Map<Integer, Map<Integer, IDoffer>> getOffersToPackageID(List<StreamingPackage> streamingPackages,
                                                                     List<StreamingPackage> otherPackages,
                                                                     Map<StreamingPackage, Set<GameOffer>> packageToGameOffers) {
        Map<Integer, Map<Integer, IDoffer>> packagesAndOffers = new HashMap<>();

        // Include best combination packages
        for (StreamingPackage sp : streamingPackages) {
            Set<GameOffer> offers = packageToGameOffers.getOrDefault(sp, Collections.emptySet());
            for (GameOffer gameOffer : offers) {
                packagesAndOffers
                        .computeIfAbsent(sp.getId(), k -> new HashMap<>())
                        .put(gameOffer.getGame().getId(), new IDoffer(gameOffer.isLive(), gameOffer.isHighlights()));
            }
        }

        // Include other packages as well
        for (StreamingPackage sp : otherPackages) {
            Set<GameOffer> offers = packageToGameOffers.getOrDefault(sp, Collections.emptySet());
            for (GameOffer gameOffer : offers) {
                packagesAndOffers
                        .computeIfAbsent(sp.getId(), k -> new HashMap<>())
                        .put(gameOffer.getGame().getId(), new IDoffer(gameOffer.isLive(), gameOffer.isHighlights()));
            }
        }

        return packagesAndOffers;
    }
}
