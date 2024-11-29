package com.gendev.streamcombinations.service.searching2;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.*;

import java.util.*;
import java.util.Map.Entry;

public class AlgorithmFewest {

    public static List<Map<Integer, Set<Integer>>> sortedList(Map<Integer, Set<Integer>> gamePackagesForTeams) {
        List<Map.Entry<Integer, Set<Integer>>> entryList = new ArrayList<>(gamePackagesForTeams.entrySet());
        entryList.sort((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()));

        // Create a sorted list of maps from the sorted entries
        List<Map<Integer, Set<Integer>>> sortedListOfMaps = new ArrayList<>();
        for (Entry<Integer, Set<Integer>> entry : entryList) {
            Map<Integer, Set<Integer>> sortedMap = new HashMap<>();
            sortedMap.put(entry.getKey(), entry.getValue());
            sortedListOfMaps.add(sortedMap);
        }

        // Print the sorted list of maps
//        for (Map<Integer, Set<Integer>> map : sortedListOfMaps) {
//            System.out.println(map);
//        }

        return sortedListOfMaps;
    }

    public static List<Map<Integer, Set<Integer>>> findNecessaryMaps(List<Map<Integer, Set<Integer>>> sortedListOfMaps, Set<Integer> gameIDs) {
        List<Map<Integer, Set<Integer>>> necessaryMaps = new ArrayList<>();
        Set<Integer> coveredIDs = new HashSet<>();

        for (Map<Integer, Set<Integer>> map : sortedListOfMaps) {
            for (Map.Entry<Integer, Set<Integer>> entry : map.entrySet()) {
                Set<Integer> set = entry.getValue();
                Set<Integer> intersection = new HashSet<>(set);
                intersection.retainAll(gameIDs);

                if (!intersection.isEmpty()) {
                    necessaryMaps.add(map);
                    coveredIDs.addAll(intersection);
                    gameIDs.removeAll(intersection);
                }

                if (gameIDs.isEmpty()) {
                    break;
                }
            }

            if (gameIDs.isEmpty()) {
                break;
            }
        }

        return necessaryMaps;
    }

    public static Map<String, List<Integer>> findPackages(Map<Integer, Set<Integer>> gamePackagesForTeams, Set<Integer> gameIDs){

        List<Map<Integer, Set<Integer>>> sortedListOfMaps = sortedList(gamePackagesForTeams);
        List<Map<Integer, Set<Integer>>> findNecessaryMaps = findNecessaryMaps(sortedListOfMaps, gameIDs);
        System.out.println("Necessary maps:");
        for (Map<Integer, Set<Integer>> map : findNecessaryMaps) {
            System.out.println(map);
        }


        return null;
    }

    public static void main(String[] args) {

    }
}
