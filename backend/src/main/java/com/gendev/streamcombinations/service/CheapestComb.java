package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.model.helper.GameOffer;
import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.*;

import java.util.*;
import java.util.stream.Collectors;

public class CheapestComb {

    public static List<StreamingPackage> findCheapestCombination(Set<Game> requiredGames,
                                                                 Map<StreamingPackage, Set<GameOffer>> packageToGameOffers) {
        Loader.loadNativeLibraries();

        // Build maps for live/highlight coverage
        Map<Game, Set<StreamingPackage>> liveCoverage = new HashMap<>();
        Map<Game, Set<StreamingPackage>> highlightsCoverage = new HashMap<>();

        for (Game game : requiredGames) {
            liveCoverage.put(game, new HashSet<>());
            highlightsCoverage.put(game, new HashSet<>());
        }

        for (Map.Entry<StreamingPackage, Set<GameOffer>> entry : packageToGameOffers.entrySet()) {
            StreamingPackage sp = entry.getKey();
            for (GameOffer go : entry.getValue()) {
                Game g = go.getGame();
                if (requiredGames.contains(g)) {
                    // Must have both live and highlights coverage for the game to be counted as covered
                    if (go.isLive()) {
                        liveCoverage.get(g).add(sp);
                    }
                    if (go.isHighlights()) {
                        highlightsCoverage.get(g).add(sp);
                    }
                }
            }
        }

        // Filter out games that cannot be fully covered (no live or no highlights)
        Set<Game> trulyRequiredGames = requiredGames.stream()
                .filter(g -> !liveCoverage.get(g).isEmpty() && !highlightsCoverage.get(g).isEmpty())
                .collect(Collectors.toSet());

        // If no coverable games remain, return empty list
        if (trulyRequiredGames.isEmpty()) {
            return new ArrayList<>();
        }

        List<StreamingPackage> allPackages = new ArrayList<>(packageToGameOffers.keySet());
        Map<StreamingPackage, Integer> pkgIndex = new HashMap<>();
        for (int i = 0; i < allPackages.size(); i++) {
            pkgIndex.put(allPackages.get(i), i);
        }

        // ---------- Phase 1: Minimize cost ----------
        MPSolver solver1 = MPSolver.createSolver("CBC");
        if (solver1 == null) {
            throw new RuntimeException("Could not create solver. Check OR-Tools installation.");
        }

        MPVariable[] x1 = new MPVariable[allPackages.size()];
        for (int i = 0; i < allPackages.size(); i++) {
            x1[i] = solver1.makeBoolVar("x_" + i);
        }

        // Constraints for coverage
        for (Game g : trulyRequiredGames) {
            // Live coverage constraint
            MPConstraint liveConstraint = solver1.makeConstraint(1, Double.POSITIVE_INFINITY, "live_" + g.getId());
            for (StreamingPackage sp : liveCoverage.get(g)) {
                liveConstraint.setCoefficient(x1[pkgIndex.get(sp)], 1.0);
            }

            // Highlights coverage constraint
            MPConstraint highlightsConstraint = solver1.makeConstraint(1, Double.POSITIVE_INFINITY, "highlights_" + g.getId());
            for (StreamingPackage sp : highlightsCoverage.get(g)) {
                highlightsConstraint.setCoefficient(x1[pkgIndex.get(sp)], 1.0);
            }
        }

        // Objective: minimize sum of costs
        MPObjective obj1 = solver1.objective();
        obj1.setMinimization();
        for (int i = 0; i < allPackages.size(); i++) {
            int cost = allPackages.get(i).getMonthly_price_yearly_subscription_in_cents();
            obj1.setCoefficient(x1[i], cost);
        }

        MPSolver.ResultStatus resultStatus1 = solver1.solve();
        if (resultStatus1 != MPSolver.ResultStatus.OPTIMAL && resultStatus1 != MPSolver.ResultStatus.FEASIBLE) {
            // No solution
            return new ArrayList<>();
        }

        double minimalCost = obj1.value();

        // ---------- Phase 2: Minimize number of packages chosen, given the minimal cost ----------
        // We will create a new solver or re-use the same with modifications.
        MPSolver solver2 = MPSolver.createSolver("CBC");
        if (solver2 == null) {
            throw new RuntimeException("Could not create second solver. Check OR-Tools installation.");
        }

        MPVariable[] x2 = new MPVariable[allPackages.size()];
        for (int i = 0; i < allPackages.size(); i++) {
            x2[i] = solver2.makeBoolVar("x2_" + i);
        }

        // Same coverage constraints
        for (Game g : trulyRequiredGames) {
            MPConstraint liveConstraint = solver2.makeConstraint(1, Double.POSITIVE_INFINITY, "live_" + g.getId());
            for (StreamingPackage sp : liveCoverage.get(g)) {
                liveConstraint.setCoefficient(x2[pkgIndex.get(sp)], 1.0);
            }

            MPConstraint highlightsConstraint = solver2.makeConstraint(1, Double.POSITIVE_INFINITY, "highlights_" + g.getId());
            for (StreamingPackage sp : highlightsCoverage.get(g)) {
                highlightsConstraint.setCoefficient(x2[pkgIndex.get(sp)], 1.0);
            }
        }

        // Add a constraint that the total cost equals the minimalCost found in phase 1
        MPConstraint costConstraint = solver2.makeConstraint(minimalCost, minimalCost, "cost_eq");
        for (int i = 0; i < allPackages.size(); i++) {
            int cost = allPackages.get(i).getMonthly_price_yearly_subscription_in_cents();
            costConstraint.setCoefficient(x2[i], cost);
        }

        // New objective: minimize number of packages chosen
        MPObjective obj2 = solver2.objective();
        obj2.setMinimization();
        for (int i = 0; i < allPackages.size(); i++) {
            // Each chosen package contributes 1 to the objective
            obj2.setCoefficient(x2[i], 1.0);
        }

        MPSolver.ResultStatus resultStatus2 = solver2.solve();
        if (resultStatus2 != MPSolver.ResultStatus.OPTIMAL && resultStatus2 != MPSolver.ResultStatus.FEASIBLE) {
            // This should not happen if phase 1 was feasible, but just in case:
            // fallback: return solution from phase 1
            List<StreamingPackage> chosen = new ArrayList<>();
            for (int i = 0; i < allPackages.size(); i++) {
                if (x1[i].solutionValue() > 0.5) {
                    chosen.add(allPackages.get(i));
                }
            }
            chosen.sort(Comparator.comparingInt(StreamingPackage::getMonthly_price_yearly_subscription_in_cents));
            return chosen;
        }

        // Extract solution from phase 2
        List<StreamingPackage> chosenPackages = new ArrayList<>();
        for (int i = 0; i < allPackages.size(); i++) {
            if (x2[i].solutionValue() > 0.5) {
                chosenPackages.add(allPackages.get(i));
            }
        }

        // Sort chosen packages by their yearly subscription monthly price
        chosenPackages.sort(Comparator.comparingInt(StreamingPackage::getMonthly_price_yearly_subscription_in_cents));

        return chosenPackages;
    }
}
