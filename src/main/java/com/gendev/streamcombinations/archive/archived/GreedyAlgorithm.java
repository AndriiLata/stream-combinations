package com.gendev.streamcombinations.archive.archived;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GreedyAlgorithm {

    private Map<Integer, Set<Integer>> streamingServiceIDs;
    private Map<Integer, Integer> servicePrices;
    private Set<Integer> desiredGameIDs;

    public GreedyAlgorithm(Map<Integer, Set<Integer>> streamingServiceIDs, Map<Integer, Integer> servicePrices, Set<Integer> desiredGameIDs) {
        this.streamingServiceIDs = streamingServiceIDs;
        this.servicePrices = servicePrices;
        this.desiredGameIDs = desiredGameIDs;
    }

    public Set<Integer> getCheapestCombination(){
        Set<Integer> selectedServices = new HashSet<>();
        Set<Integer> coveredGames = new HashSet<>();

        while(!coveredGames.containsAll(desiredGameIDs)){
            int bestService = -1;
            Set<Integer> bestServiceGames = new HashSet<>();
            int bestCostEffectiveness = Integer.MAX_VALUE;

            for(Map.Entry<Integer, Set<Integer>> entry: streamingServiceIDs.entrySet()){
                int serviceID = entry.getKey();
                Set<Integer> games = new HashSet<>(entry.getValue());
                games.removeAll(coveredGames); // Only consider games that are not already covered

                int price = servicePrices.get(serviceID);
                int numberOfUncoveredGames = games.size();

                if(numberOfUncoveredGames > 0){
                    int costEffectiveness = price / numberOfUncoveredGames;
                    if(costEffectiveness < bestCostEffectiveness){
                        bestService = serviceID;
                        bestServiceGames = games;
                        bestCostEffectiveness = costEffectiveness;
                    }
                }
            }

            if (bestService == -1){
                System.out.println("No service found that covers all desired games");
                break;
            }

            selectedServices.add(bestService);
            coveredGames.addAll(bestServiceGames);
//            desiredGameIDs.removeAll(bestServiceGames);
        }


        return selectedServices;
    }
}
