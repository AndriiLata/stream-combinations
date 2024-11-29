package com.gendev.streamcombinations.archive.searching;

import java.util.*;

public class AlgorithmFewest {

    public static List<Map.Entry<Integer, Set<Integer>>> sortedList(Map<Integer, Set<Integer>> gamePackagesForTeams) {
        // Convert the map to a list of entries and sort by the size of the set in descending order
        List<Map.Entry<Integer, Set<Integer>>> entryList = new ArrayList<>(gamePackagesForTeams.entrySet());
        entryList.sort((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()));

        return entryList;
    }



    public static Map<String, Object> findPackages(Map<Integer, Set<Integer>> gamePackagesForTeams, Set<Integer> gameIDs){

        List<Map.Entry<Integer, Set<Integer>>> sortedListOfEntries = sortedList(gamePackagesForTeams);

        List<Map.Entry<Integer, Set<Integer>>> necessaryEntries = new ArrayList<>();
        Set<Integer> coveredIDs = new HashSet<>();

        for (Map.Entry<Integer, Set<Integer>> entry : sortedListOfEntries) {
            Set<Integer> set = entry.getValue();
            Set<Integer> intersection = new HashSet<>(set);
            intersection.retainAll(gameIDs);

            if (!intersection.isEmpty()) {
                necessaryEntries.add(entry);
                coveredIDs.addAll(intersection);
                gameIDs.removeAll(intersection);
            }

            if (gameIDs.isEmpty()) {
                break;
            }
        }

//        gameIDs.removeAll(coveredIDs);

        Map<String, Object> result = new HashMap<>();
        result.put("necessaryPackages", necessaryEntries);
        result.put("uncoveredGames", gameIDs);
        return result;
    }
}
