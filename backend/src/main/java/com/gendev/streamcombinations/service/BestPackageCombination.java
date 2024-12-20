package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.model.helper.GameOffer;
import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.*;

import java.util.*;

public class BestPackageCombination {

    public static List<StreamingPackage> findCheapestCombination(Set<Game> requiredGames,
                                                                 Map<StreamingPackage, Set<GameOffer>> packageToGameOffers,
                                                                 List<StreamingPackage> allPackages,
                                                                 int[] costs) {
        Loader.loadNativeLibraries();

        Map<StreamingPackage, Integer> pkgIndexMap = indexPackages(allPackages);

        // Coverage Maps
        Map<Game, Set<StreamingPackage>> liveCoverage = new HashMap<>();
        Map<Game, Set<StreamingPackage>> highlightsCoverage = new HashMap<>();
        prepareCoverage(requiredGames, packageToGameOffers, liveCoverage, highlightsCoverage);

        Set<Game> trulyRequiredGames = filterCoverableGames(requiredGames, liveCoverage, highlightsCoverage);
        if (trulyRequiredGames.isEmpty()) {
            return new ArrayList<>();
        }

        // Phase 1: Minimierung Kosten
        MPSolver solver1 = createSolver();
        MPVariable[] x1 = createDecisionVariables(solver1, allPackages.size());
        addCoverageConstraints(solver1, x1, trulyRequiredGames, liveCoverage, highlightsCoverage, pkgIndexMap);

        MPObjective obj1 = solver1.objective();
        obj1.setMinimization();
        for (int i = 0; i < allPackages.size(); i++) {
            obj1.setCoefficient(x1[i], costs[i]);
        }

        MPSolver.ResultStatus status1 = solver1.solve();
        if (status1 != MPSolver.ResultStatus.OPTIMAL && status1 != MPSolver.ResultStatus.FEASIBLE) {
            return new ArrayList<>();
        }
        double minimalCost = obj1.value();

        // Phase 2: Minimale Anzahl Pakete
        MPSolver solver2 = createSolver();
        MPVariable[] x2 = createDecisionVariables(solver2, allPackages.size());
        addCoverageConstraints(solver2, x2, trulyRequiredGames, liveCoverage, highlightsCoverage, pkgIndexMap);

        MPConstraint costEq = solver2.makeConstraint(minimalCost, minimalCost, "cost_eq");
        for (int i = 0; i < allPackages.size(); i++) {
            costEq.setCoefficient(x2[i], costs[i]);
        }

        MPObjective obj2 = solver2.objective();
        obj2.setMinimization();
        for (MPVariable var : x2) {
            obj2.setCoefficient(var, 1.0);
        }

        MPSolver.ResultStatus status2 = solver2.solve();
        List<StreamingPackage> chosenPackages;
        if (status2 == MPSolver.ResultStatus.OPTIMAL || status2 == MPSolver.ResultStatus.FEASIBLE) {
            chosenPackages = extractChosenPackages(x2, allPackages);
        } else {
            // Fallback auf Phase1 Lösung
            chosenPackages = extractChosenPackages(x1, allPackages);
        }

        sortByGamesCovered(chosenPackages, packageToGameOffers, trulyRequiredGames);
        return chosenPackages;
    }



    private static void prepareCoverage(Set<Game> requiredGames,
                                        Map<StreamingPackage, Set<GameOffer>> packageToGameOffers,
                                        Map<Game, Set<StreamingPackage>> liveCoverage,
                                        Map<Game, Set<StreamingPackage>> highlightsCoverage) {

        for (Game g : requiredGames) {
            liveCoverage.put(g, new HashSet<>());
            highlightsCoverage.put(g, new HashSet<>());
        }


        for (Map.Entry<StreamingPackage, Set<GameOffer>> entry : packageToGameOffers.entrySet()) {
            StreamingPackage sp = entry.getKey();
            Set<GameOffer> offers = entry.getValue();

            for (GameOffer go : offers) {
                Game g = go.getGame();
                if (requiredGames.contains(g)) {
                    if (go.isLive()) {
                        liveCoverage.get(g).add(sp);
                    }
                    if (go.isHighlights()) {
                        highlightsCoverage.get(g).add(sp);
                    }
                }
            }
        }
    }

    // Games filtern die nicht gecovered werden können
    private static Set<Game> filterCoverableGames(Set<Game> requiredGames,
                                                  Map<Game, Set<StreamingPackage>> liveCoverage,
                                                  Map<Game, Set<StreamingPackage>> highlightsCoverage) {

        Set<Game> result = new HashSet<>(requiredGames.size());
        for (Game g : requiredGames) {
            Set<StreamingPackage> live = liveCoverage.get(g);
            Set<StreamingPackage> highlights = highlightsCoverage.get(g);
            if (live != null && !live.isEmpty() && highlights != null && !highlights.isEmpty()) {
                result.add(g);
            }
        }
        return result;
    }


    private static Map<StreamingPackage, Integer> indexPackages(List<StreamingPackage> allPackages) {
        Map<StreamingPackage, Integer> pkgIndex = new HashMap<>(allPackages.size());
        for (int i = 0; i < allPackages.size(); i++) {
            pkgIndex.put(allPackages.get(i), i);
        }
        return pkgIndex;
    }


    private static MPSolver createSolver() {
        MPSolver solver = MPSolver.createSolver("CBC");
        if (solver == null) {
            throw new RuntimeException("Could not create solver. Check OR-Tools installation.");
        }
        return solver;
    }

    private static MPVariable[] createDecisionVariables(MPSolver solver, int count) {
        MPVariable[] vars = new MPVariable[count];
        for (int i = 0; i < count; i++) {
            vars[i] = solver.makeBoolVar("x_" + i);
        }
        return vars;
    }


    private static void addCoverageConstraints(MPSolver solver,
                                               MPVariable[] x,
                                               Set<Game> trulyRequiredGames,
                                               Map<Game, Set<StreamingPackage>> liveCoverage,
                                               Map<Game, Set<StreamingPackage>> highlightsCoverage,
                                               Map<StreamingPackage, Integer> pkgIndexMap) {

        for (Game g : trulyRequiredGames) {
            // Live coverage
            MPConstraint liveC = solver.makeConstraint(1, Double.POSITIVE_INFINITY, "live_" + g.getId());
            for (StreamingPackage sp : liveCoverage.get(g)) {
                liveC.setCoefficient(x[pkgIndexMap.get(sp)], 1.0);
            }

            // Highlights coverage
            MPConstraint highlightsC = solver.makeConstraint(1, Double.POSITIVE_INFINITY, "highlights_" + g.getId());
            for (StreamingPackage sp : highlightsCoverage.get(g)) {
                highlightsC.setCoefficient(x[pkgIndexMap.get(sp)], 1.0);
            }
        }
    }


    private static double solvePhase1(MPSolver solver1, MPVariable[] x1, List<StreamingPackage> allPackages) {
        MPObjective obj1 = solver1.objective();
        obj1.setMinimization();
        for (int i = 0; i < allPackages.size(); i++) {
            obj1.setCoefficient(x1[i], allPackages.get(i).getMonthly_price_yearly_subscription_in_cents());
        }

        MPSolver.ResultStatus status = solver1.solve();
        if (status != MPSolver.ResultStatus.OPTIMAL && status != MPSolver.ResultStatus.FEASIBLE) {
            return Double.NaN;
        }
        return obj1.value();
    }


    private static List<StreamingPackage> solvePhase2(MPSolver solver2,
                                                      MPVariable[] x2,
                                                      List<StreamingPackage> allPackages,
                                                      double minimalCost) {
        // Add cost equality constraint
        MPConstraint costEq = solver2.makeConstraint(minimalCost, minimalCost, "cost_eq");
        for (int i = 0; i < allPackages.size(); i++) {
            costEq.setCoefficient(x2[i], allPackages.get(i).getMonthly_price_yearly_subscription_in_cents());
        }

        // New objective: minimize number of chosen packages
        MPObjective obj2 = solver2.objective();
        obj2.setMinimization();
        for (MPVariable var : x2) {
            obj2.setCoefficient(var, 1.0);
        }

        MPSolver.ResultStatus status = solver2.solve();
        if (status != MPSolver.ResultStatus.OPTIMAL && status != MPSolver.ResultStatus.FEASIBLE) {
            return Collections.emptyList();
        }

        return extractChosenPackages(x2, allPackages);
    }

    /**
     * Extract chosen packages from the solution variables.
     */
    private static List<StreamingPackage> extractChosenPackages(MPVariable[] x, List<StreamingPackage> allPackages) {
        List<StreamingPackage> chosen = new ArrayList<>();
        for (int i = 0; i < allPackages.size(); i++) {
            if (x[i].solutionValue() > 0.5) {
                chosen.add(allPackages.get(i));
            }
        }
        return chosen;
    }

    /**
     * Sorts the chosen packages by the number of required games they cover in descending order.
     */
    private static void sortByGamesCovered(List<StreamingPackage> chosenPackages,
                                           Map<StreamingPackage, Set<GameOffer>> packageToGameOffers,
                                           Set<Game> trulyRequiredGames) {
        // Precompute coverage counts efficiently
        // For performance: use a HashSet to count distinct games covered by a package
        // (Though we expect no duplicates per package->game pair, we avoid stream distinct)
        int size = chosenPackages.size();
        int[] coverageCounts = new int[size];

        // We'll store trulyRequiredGames in a HashSet for O(1) contains
        // It's likely already a HashSet, but let's ensure:
        HashSet<Game> requiredSet = (trulyRequiredGames instanceof HashSet)
                ? (HashSet<Game>) trulyRequiredGames
                : new HashSet<>(trulyRequiredGames);

        for (int i = 0; i < size; i++) {
            StreamingPackage sp = chosenPackages.get(i);
            Set<GameOffer> offers = packageToGameOffers.get(sp);
            if (offers == null || offers.isEmpty()) {
                coverageCounts[i] = 0;
                continue;
            }
            int count = 0;
            // A small optimization: since we know each GameOffer has a unique Game,
            // we can just check if that game is in requiredSet.
            // No need for distinct counting since each GameOffer is unique to a game.
            for (GameOffer go : offers) {
                if (requiredSet.contains(go.getGame())) {
                    count++;
                }
            }
            coverageCounts[i] = count;
        }

        // Sort by coverage descending
        // We'll sort indices and then reorder chosenPackages
        Integer[] indices = new Integer[size];
        for (int i = 0; i < size; i++) indices[i] = i;

        Arrays.sort(indices, (a, b) -> Integer.compare(coverageCounts[b], coverageCounts[a]));

        // Rebuild chosenPackages in sorted order
        List<StreamingPackage> sorted = new ArrayList<>(size);
        for (int idx : indices) {
            sorted.add(chosenPackages.get(idx));
        }
        chosenPackages.clear();
        chosenPackages.addAll(sorted);
    }
}
