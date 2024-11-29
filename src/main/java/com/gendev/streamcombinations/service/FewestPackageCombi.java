package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.service.searching.SearchAlgorithmFewest;
import com.gendev.streamcombinations.service.searching2.AlgorithmFewest;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FewestPackageCombi {
    private final StructureData sd;

    public FewestPackageCombi() {
        this.sd = new StructureData();
    }

    public Map<String, List<Integer>> fewestPackagesForTeams(List<String> teams) {
        Map<Integer, Set<Integer>> gPFT = sd.gamePackagesForTeams(teams);
        Set<Integer> gameIDs = sd.findGameIdsForTeams(teams);

        return AlgorithmFewest.findPackages(gPFT, gameIDs);
    }

    public static void main(String[] args) {
        FewestPackageCombi fpc = new FewestPackageCombi();
        List<String> teams = List.of("Bayern München");

        Map<String, List<Integer>> result = fpc.fewestPackagesForTeams(teams);

//        System.out.println("Ranked Packages: " + result.get("rankedPackages"));
//        System.out.println("Uncovered Games: " + result.get("uncoveredGames"));
//        System.out.println("minimum Packages" + result.get("minimumPackages"));
    }

}
