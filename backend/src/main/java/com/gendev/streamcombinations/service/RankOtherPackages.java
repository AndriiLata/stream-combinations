package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.model.helper.GameOffer;
import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import com.gendev.streamcombinations.util.SubscriptionCostUtil;

import java.util.*;

public class RankOtherPackages {
    public static List<StreamingPackage> rankOtherPackages(Set<Game> requiredGames,
                                                           Map<StreamingPackage, Set<GameOffer>> packageToGameOffers,
                                                           List<StreamingPackage> chosenPackages,
                                                           int monthsDifference) {
        Set<StreamingPackage> chosenSet = new HashSet<>(chosenPackages);

        List<StreamingPackage> others = new ArrayList<>();
        for (StreamingPackage sp : packageToGameOffers.keySet()) {
            if (!chosenSet.contains(sp)) {
                others.add(sp);
            }
        }

        List<ScoredPackage> scored = new ArrayList<>();
        for (StreamingPackage sp : others) {
            double score = calculateScore(sp, packageToGameOffers, requiredGames);
            int totalCost = SubscriptionCostUtil.calculateTotalCost(sp, monthsDifference);
            scored.add(new ScoredPackage(sp, score, totalCost));
        }

        scored.sort((a, b) -> {
            int comp = Double.compare(b.score, a.score);
            if (comp == 0) {
                return Integer.compare(a.totalCost, b.totalCost);
            }
            return comp;
        });

        List<StreamingPackage> sortedOthers = new ArrayList<>();
        for (ScoredPackage sc : scored) {
            sortedOthers.add(sc.sp);
        }

        return sortedOthers;
    }

    private static double calculateScore(StreamingPackage sp,
                                         Map<StreamingPackage, Set<GameOffer>> packageToGameOffers,
                                         Set<Game> requiredGames) {
        Set<GameOffer> offers = packageToGameOffers.get(sp);
        double score = 0.0;
        if (offers != null) {
            for (GameOffer go : offers) {
                if (requiredGames.contains(go.getGame())) {
                    double gameScore = 0.0;
                    if (go.isLive()) gameScore += 5.0;
                    if (go.isHighlights()) gameScore += 0.5;
                    score += gameScore;
                }
            }
        }
        return score;
    }

    private static class ScoredPackage {
        StreamingPackage sp;
        double score;
        int totalCost;

        ScoredPackage(StreamingPackage sp, double score, int totalCost) {
            this.sp = sp;
            this.score = score;
            this.totalCost = totalCost;
        }
    }

}
