package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.model.helper.GameOffer;
import com.gendev.streamcombinations.model.main.*;
import com.gendev.streamcombinations.model.main.StreamingPackage;

import java.util.*;

public class RankOtherPackages {
    public static List<StreamingPackage> rankOtherPackages(Set<Game> requiredGames,
                                                    Map<StreamingPackage, Set<GameOffer>> packageToGameOffers,
                                                    List<StreamingPackage> chosenPackages) {
        // Convert chosenPackages to a set for quick lookup
        Set<StreamingPackage> chosenSet = new HashSet<>(chosenPackages);

        // Build a list of other packages
        List<StreamingPackage> others = new ArrayList<>();
        for (StreamingPackage sp : packageToGameOffers.keySet()) {
            if (!chosenSet.contains(sp)) {
                others.add(sp);
            }
        }

        // Score packages
        List<ScoredPackage> scored = new ArrayList<>();
        for (StreamingPackage sp : others) {
            Set<GameOffer> offers = packageToGameOffers.get(sp);
            double score = 0.0;
            if (offers != null) {
                for (GameOffer go : offers) {
                    // Only consider required games for scoring
                    if (requiredGames.contains(go.getGame())) {
                        double gameScore = 0.0;
                        if (go.isLive()) gameScore += 5.0;
                        if (go.isHighlights()) gameScore += 0.5;
                        score += gameScore;
                    }
                }
            }
            scored.add(new ScoredPackage(sp, score));
        }

        // Sort by score descending, then by price ascending
        scored.sort((a, b) -> {
            int comp = Double.compare(b.score, a.score); // descending by score
            if (comp == 0) {
                // If tie in score, sort by yearly subscription price ascending
                return Integer.compare(a.sp.getMonthly_price_yearly_subscription_in_cents(),
                        b.sp.getMonthly_price_yearly_subscription_in_cents());
            }
            return comp;
        });

        // Extract sorted packages
        List<StreamingPackage> sortedOthers = new ArrayList<>();
        for (ScoredPackage sc : scored) {
            sortedOthers.add(sc.sp);
        }

        return sortedOthers;
    }
    private static class ScoredPackage {
        StreamingPackage sp;
        double score;

        ScoredPackage(StreamingPackage sp, double score) {
            this.sp = sp;
            this.score = score;
        }
    }

}


