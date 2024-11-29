package com.gendev.streamcombinations.service.searching;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.*;

import java.util.*;

public class SearchAlgorithm {

    public static Map<String, Object> findOptimalPackages(Map<Integer, Set<Integer>> gamePackages,
                                                          Map<Integer, Integer> packagePrices,
                                                          Set<Integer> allGames) {
        Loader.loadNativeLibraries(); // Ensure OR-Tools native libraries are loaded

        // Pre-check: Identify games that cannot be covered
        Set<Integer> allCoveredGames = new HashSet<>();
        for (Set<Integer> packageGames : gamePackages.values()) {
            allCoveredGames.addAll(packageGames);
        }

        // Separate uncovered games from the rest
        Set<Integer> uncoveredGames = new HashSet<>(allGames);
        uncoveredGames.removeAll(allCoveredGames);

        // Adjust allGames to only include those that can be covered
        Set<Integer> solvableGames = new HashSet<>(allGames);
        solvableGames.removeAll(uncoveredGames);

        MPSolver solver = MPSolver.createSolver("SCIP");
        if (solver == null) {
            throw new RuntimeException("Failed to create OR-Tools solver.");
        }

        // Create variables for each package (binary variables)
        Map<Integer, MPVariable> variables = new HashMap<>();
        for (Integer packageId : gamePackages.keySet()) {
            variables.put(packageId, solver.makeBoolVar("x_" + packageId));
        }

        // Add objective: Minimize the weighted sum of total price and number of services
        MPObjective objective = solver.objective();
        double weightCost = 1.0;  // Weight for cost (primary objective)
        double weightCount = 0.01; // Small weight for minimizing the number of services
        for (Map.Entry<Integer, MPVariable> entry : variables.entrySet()) {
            Integer packageId = entry.getKey();
            MPVariable variable = entry.getValue();
            // Objective = weightCost * price + weightCount * number_of_services
            objective.setCoefficient(variable, weightCost * packagePrices.get(packageId) + weightCount);
        }
        objective.setMinimization();

        // Add constraints: Each solvable game must be covered by at least one package
        for (Integer game : solvableGames) {
            MPConstraint constraint = solver.makeConstraint(1, Double.POSITIVE_INFINITY, "game_" + game);
            for (Map.Entry<Integer, Set<Integer>> entry : gamePackages.entrySet()) {
                if (entry.getValue().contains(game)) {
                    constraint.setCoefficient(variables.get(entry.getKey()), 1);
                }
            }
        }

        // Solve the problem
        MPSolver.ResultStatus resultStatus = solver.solve();
        if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
            throw new RuntimeException("No optimal solution found.");
        }

        // Extract the selected packages
        Set<Integer> selectedPackages = new HashSet<>();
        for (Map.Entry<Integer, MPVariable> entry : variables.entrySet()) {
            if (entry.getValue().solutionValue() > 0.5) { // Check if the variable is selected
                selectedPackages.add(entry.getKey());
            }
        }

        // Prepare the result
        Map<String, Object> result = new HashMap<>();
        result.put("selectedPackages", selectedPackages);
        result.put("uncoveredGames", uncoveredGames); // Include uncovered games for reporting

        return result;
    }



}
