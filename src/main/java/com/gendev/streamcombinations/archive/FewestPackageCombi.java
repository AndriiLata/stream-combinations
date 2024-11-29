package com.gendev.streamcombinations.archive;

import com.gendev.streamcombinations.archive.searching.AlgorithmFewest;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FewestPackageCombi {
    private final StructureData sd;

    public FewestPackageCombi() {
        this.sd = new StructureData();
    }

    public Map<String, Object> fewestPackagesForTeams(List<String> teams) {
        Map<Integer, Set<Integer>> gPFT = sd.gamePackagesForTeams(teams);
        Set<Integer> gameIDs = sd.findGameIdsForTeams(teams);

        return AlgorithmFewest.findPackages(gPFT, gameIDs);
    }
    /* ToDo: I want that this class give me a map of:
        -uncovered games (just for me to know)
        -necessary packages ->what games are covered by each package
        -price of each package (maybe do it somewhere else)

     */

    public static void main(String[] args) {
        FewestPackageCombi fpc = new FewestPackageCombi();
        List<String> teams = List.of("Hatayspor", "Deutschland", "Bayern München");

        Map<String, Object> result = fpc.fewestPackagesForTeams(teams);

        System.out.println("Necessary Packages: " + result.get("necessaryPackages"));
        System.out.println("Uncovered Games: " + result.get("uncoveredGames"));

    }

}
