package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.GameOffer;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CheapestCombinationAlgorithm {

    public static List<StreamingPackage> leastServicesAlgorithm(
            Set<Game> requiredGames,
            Map<StreamingPackage, Set<GameOffer>> packageToGameOffers) {

        Set<Game> coveredGames = new HashSet<>();
        Set<StreamingPackage> selectedPackages = new HashSet<>();
        Set<Game> remainingGames = new HashSet<>(requiredGames);

        while (!remainingGames.isEmpty()) {
            StreamingPackage bestPackage = null;
            Set<Game> bestPackageCovers = new HashSet<>();
            double bestCostPerQuality = Double.MAX_VALUE;

            for (Map.Entry<StreamingPackage, Set<GameOffer>> entry : packageToGameOffers.entrySet()) {
                StreamingPackage streamingPackage = entry.getKey();

                if (selectedPackages.contains(streamingPackage)) {
                    continue;
                }

                Set<GameOffer> gameOffers = entry.getValue();
                Set<Game> packageCovers = new HashSet<>();
                int totalQuality = 0;

                for (GameOffer gameOffer : gameOffers) {
                    Game game = gameOffer.getGame();

                    if (remainingGames.contains(game)) {
                        packageCovers.add(game);

                        int quality = 0;
                        if (gameOffer.isLive() && gameOffer.isHighlights()) {
                            quality = 300;
                        } else if (gameOffer.isLive()) {
                            quality = 200;
                        } else if (gameOffer.isHighlights()) {
                            quality = 1;
                        }

                        totalQuality += quality;
                    }
                }

                if (!packageCovers.isEmpty()) {
                    // Calculate cost per unit of quality
                    double costPerQuality = (((double) streamingPackage.getMonthly_price_yearly_subscription_in_cents())+ 100 ) / totalQuality;

                    if (costPerQuality < bestCostPerQuality) {
                        bestPackage = streamingPackage;
                        bestPackageCovers = packageCovers;
                        bestCostPerQuality = costPerQuality;
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

