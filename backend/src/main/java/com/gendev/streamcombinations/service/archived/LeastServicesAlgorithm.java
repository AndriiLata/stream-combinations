package com.gendev.streamcombinations.service.archived;

import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.helper.GameOffer;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LeastServicesAlgorithm {
    public static List<StreamingPackage> leastServicesAlgorithm(
            Set<Game> requiredGames,
            Map<StreamingPackage, Set<GameOffer>> packageToGameOffers) {

        Set<Game> coveredGames = new HashSet<>();
        Set<StreamingPackage> selectedPackages = new HashSet<>();
        Set<Game> remainingGames = new HashSet<>(requiredGames);

        while (!remainingGames.isEmpty()) {
            StreamingPackage bestPackage = null;
            Set<Game> bestPackageCovers = new HashSet<>();
            int bestPackageQuality = -1;

            for (Map.Entry<StreamingPackage, Set<GameOffer>> entry : packageToGameOffers.entrySet()) {
                StreamingPackage streamingPackage = entry.getKey();

                if (selectedPackages.contains(streamingPackage)) {
                    continue;
                }

                Set<GameOffer> gameOffers = entry.getValue();
                Set<Game> packageCovers = new HashSet<>();
                int packageQuality = 0;

                for (GameOffer gameOffer : gameOffers) {
                    Game game = gameOffer.getGame();

                    if (remainingGames.contains(game)) {
                        packageCovers.add(game);

                        int quality = 0;
                        if (gameOffer.isLive() && gameOffer.isHighlights()) {
                            quality = 3;
                        } else if (gameOffer.isLive()) {
                            quality = 2;
                        } else if (gameOffer.isHighlights()) {
                            quality = 1;
                        }

                        packageQuality += quality;
                    }
                }

                if (!packageCovers.isEmpty()) {
                    if (bestPackage == null ||
                            packageCovers.size() > bestPackageCovers.size() ||
                            (packageCovers.size() == bestPackageCovers.size() && packageQuality > bestPackageQuality)) {
                        bestPackage = streamingPackage;
                        bestPackageCovers = packageCovers;
                        bestPackageQuality = packageQuality;
                    }
                }
            }

            if (bestPackage == null) {
                // No more packages can cover remaining games
                break;
            }

            selectedPackages.add(bestPackage);
            coveredGames.addAll(bestPackageCovers);
            remainingGames.removeAll(bestPackageCovers);
        }

        return new ArrayList<>(selectedPackages);
    }
}
