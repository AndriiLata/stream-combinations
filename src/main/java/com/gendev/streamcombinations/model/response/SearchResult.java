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


    public SearchResult(Set<Game> games, List<StreamingPackage> streamingPackages,
                        Map<StreamingPackage, Set<GameOffer>> packageToGameOffers) {
        this.gamesByTournament = getGamesByTournament(games);
        this.streamingPackages = streamingPackages;
        this.offersToPackageID = getOffersToPackageID(streamingPackages, packageToGameOffers);
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
                                                                     Map<StreamingPackage, Set<GameOffer>> packageToGameOffers) {
        Map<Integer, Map<Integer, IDoffer>> packagesAndOffers = new HashMap<>();

        for (StreamingPackage streamingPackage : streamingPackages) {
            packageToGameOffers.get(streamingPackage).forEach(gameOffer -> {
                packagesAndOffers
                        .computeIfAbsent(streamingPackage.getId(), k -> new HashMap<>())
                        .put(gameOffer.getGame().getId(), new IDoffer(gameOffer.isLive(), gameOffer.isHighlights()));
            });
        }
        return packagesAndOffers;

    }

}
