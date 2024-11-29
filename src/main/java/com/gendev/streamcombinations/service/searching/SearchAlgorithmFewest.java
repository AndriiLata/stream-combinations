package com.gendev.streamcombinations.service.searching;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.*;

import java.util.*;

public class SearchAlgorithmFewest {

    public static Map<String, List<Integer>>  findPackages(Map<Integer, Set<Integer>> gamePackagesForTeams, Set<Integer> gameIDs) {
        // Load the native libraries
        Loader.loadNativeLibraries();

        Map<String, List<Integer>> result = new HashMap<>();

        // Step 1: Compute rankedPackages
        Map<Integer, Integer> packageCoverageCount = new HashMap<>();
        Map<Integer, Set<Integer>> relevantPackages = new HashMap<>();

        Set<Integer> coveredGames = new HashSet<>();

        for (Map.Entry<Integer, Set<Integer>> entry : gamePackagesForTeams.entrySet()) {
            Integer packageId = entry.getKey();
            Set<Integer> packageGames = entry.getValue();

            // Compute the intersection with gameIDs
            Set<Integer> intersection = new HashSet<>(packageGames);
            intersection.retainAll(gameIDs);

            int coverageCount = intersection.size();

            // Include all packages, even if they cover 0 games
            packageCoverageCount.put(packageId, coverageCount);
            relevantPackages.put(packageId, intersection);

            if (coverageCount > 0) {
                coveredGames.addAll(intersection);
            }
        }

        // Sort the packages in decreasing order of coverage count
        List<Integer> rankedPackages = new ArrayList<>(packageCoverageCount.keySet());
        rankedPackages.sort((a, b) -> packageCoverageCount.get(b).compareTo(packageCoverageCount.get(a)));

        result.put("rankedPackages", rankedPackages);

        // Assign ranks to packages
        Map<Integer, Integer> packageRank = new HashMap<>();
        for (int i = 0; i < rankedPackages.size(); i++) {
            int packageId = rankedPackages.get(i);
            int rank = i + 1; // Rank starts from 1
            packageRank.put(packageId, rank);
        }

        // Step 2: Identify uncovered games
        Set<Integer> uncoveredGames = new HashSet<>(gameIDs);
        uncoveredGames.removeAll(coveredGames);

        if (!uncoveredGames.isEmpty()) {
            // Some games cannot be covered
            result.put("uncoveredGames", new ArrayList<>(uncoveredGames));
        }

        // Step 3: Formulate and solve the ILP
        // Create the solver
        MPSolver solver = MPSolver.createSolver("SCIP");

        if (solver == null) {
            System.err.println("Could not create solver SCIP");
            return result;
        }

        // Variables: x_p ∈ {0,1} for each package p
        Map<Integer, MPVariable> variables = new HashMap<>();
        for (Integer packageId : relevantPackages.keySet()) {
            MPVariable var = solver.makeIntVar(0, 1, "x_" + packageId);
            variables.put(packageId, var);
        }

        // Constraints: For each game g ∈ gameIDs, sum over p (if g ∈ package p) x_p >= 1
        for (Integer gameId : gameIDs) {
            // Skip if the game is uncovered
            if (uncoveredGames.contains(gameId)) {
                continue;
            }

            MPConstraint constraint = solver.makeConstraint(1, Double.POSITIVE_INFINITY, "game_" + gameId);

            for (Map.Entry<Integer, Set<Integer>> entry : relevantPackages.entrySet()) {
                Integer packageId = entry.getKey();
                Set<Integer> packageGames = entry.getValue();

                if (packageGames.contains(gameId)) {
                    constraint.setCoefficient(variables.get(packageId), 1);
                }
            }
        }

        // Objective: Minimize total number of packages selected, prefer higher-ranked packages
        MPObjective objective = solver.objective();

        int totalPackages = variables.size();
        double epsilon = 1.0 / (totalPackages * totalPackages);

        for (Integer packageId : variables.keySet()) {
            MPVariable var = variables.get(packageId);
            int rank = packageRank.get(packageId);
            double cost = 1.0 + epsilon * rank;
            objective.setCoefficient(var, cost);
        }

        objective.setMinimization();

        // Solve the problem
        MPSolver.ResultStatus resultStatus = solver.solve();

        if (resultStatus == MPSolver.ResultStatus.OPTIMAL || resultStatus == MPSolver.ResultStatus.FEASIBLE) {
            // Get the selected packages
            List<Integer> minimumPackages = new ArrayList<>();
            for (Map.Entry<Integer, MPVariable> entry : variables.entrySet()) {
                if (entry.getValue().solutionValue() > 0.5) {
                    minimumPackages.add(entry.getKey());
                }
            }

            // Sort minimumPackages based on rankedPackages order
            minimumPackages.sort(Comparator.comparingInt(rankedPackages::indexOf));

            result.put("minimumPackages", minimumPackages);
        } else {
            System.err.println("No feasible solution found.");
            // Handle infeasibility if needed
        }

        return result;
    }
}
