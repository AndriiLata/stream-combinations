package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.service.searching.SearchAlgorithmCheapest;

import java.util.List;
import java.util.Map;
import java.util.Set;

// This class is responsible for finding the cheapest combination of packages for single or multiple teams.
public class CheapestPackageCombi {
    private final StructureData sd;

    public CheapestPackageCombi() {
        this.sd = new StructureData();
    }

    // Use String -> "Optimal Packages: " / "Uncovered Games: "
    public Map<String, Set<Integer>> cheapestPackagesForTeams(List<String> teams) {
        Map<Integer, Set<Integer>> gPFT = sd.gamePackagesForTeams(teams);
        // ToDo: So far it is only yearly subscription, in future it should be possible to choose between monthly and yearly subscription
        Map<Integer, Integer> servicePricesYearlySubscription = sd.servicePricesYearlySubscription();
        Set<Integer> gameIDs = sd.findGameIdsForTeams(teams);

        return SearchAlgorithmCheapest.findOptimalPackages(gPFT, servicePricesYearlySubscription, gameIDs);
    }


    public static void main(String[] args) {
        CheapestPackageCombi cpc = new CheapestPackageCombi();
        List<String> teams = List.of("Bayern München");

        Map<String, Set<Integer>> result = cpc.cheapestPackagesForTeams(teams);

        System.out.println("Optimal Packages: " + result.get("selectedPackages"));
        System.out.println("Uncovered Games: " + result.get("uncoveredGames"));
    }


}
