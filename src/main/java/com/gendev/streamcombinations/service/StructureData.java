package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.model.Game;
import com.gendev.streamcombinations.model.StreamingOffer;
import com.gendev.streamcombinations.model.StreamingPackage;

import java.util.*;

public class StructureData {

    private List<Game> games;
    private List<StreamingOffer> sOffers;
    private List<StreamingPackage> sPackeges;

    public StructureData(){

        FetchData fd = new FetchData();
        this.games = fd.getGames();
        this.sOffers = fd.getStreamingOffers();
        this.sPackeges = fd.getStreamingPackages();
    }

    //Returns all gameIDs for a specific teamS
    public Set<Integer> findGameIdsForTeams(List<String> teams) {
        Set<Integer> gameIDs = new HashSet<>();
        for (Game game : this.games) {
            for (String team : teams) {
                if (game.getTeam_home().equals(team) || game.getTeam_away().equals(team)) {
                    gameIDs.add(game.getId());
                    break; // No need to check other teams for this game
                }
            }
        }
        return gameIDs;
    }

    //Set of all Game IDs that this streamingPackage is streaming
    public Set<Integer> findGameIdsForPackage(int packageID){
        Set<Integer> gameIDs = new HashSet<>();
        for(StreamingOffer sOffer: this.sOffers){
            if(sOffer.getStreaming_package_id() == packageID){
                gameIDs.add(sOffer.getId());
            }
        }
        return gameIDs;
    }

    //Set of game IDs this Package cover for this team
    public Set<Integer> intersectionIDs(List<String> teams, int packageID){
        Set<Integer> gameIDsTeam = findGameIdsForTeams(teams);
        Set<Integer> gameIDsPackage = findGameIdsForPackage(packageID);

        Set<Integer> intersectionIDs = new HashSet<>(gameIDsPackage);

        intersectionIDs.retainAll(gameIDsTeam);

        return intersectionIDs;
    }
 //The first Integer tells the id of the service and
 // Set<Integer> tells what games - of the teams I am interested in - this service is streaming.
    public Map<Integer, Set<Integer>> gamePackagesForTeams(List<String> teams) {
        Map<Integer, Set<Integer>> gpft = new HashMap<>();

        for (StreamingPackage sp : this.sPackeges) {
            Set<Integer> gameIDs = intersectionIDs(teams, sp.getId());
            if (!gameIDs.isEmpty()) {
                gpft.put(sp.getId(), gameIDs);
            }
        }

        return gpft;
    }

    // ToDo: So far it is only yearly subscription, in future it should be possible to choose between monthly and yearly subscription
    public Map<Integer, Integer> servicePricesYearlySubscription(){
        Map<Integer, Integer> servicePrices = new HashMap<>();
        for(StreamingPackage sp: this.sPackeges){
            servicePrices.put(sp.getId(), sp.getMonthly_price_yearly_subscription_in_cents());
        }
        return servicePrices;
    }

    public static void main(String[] args) {
        StructureData sd = new StructureData();
        List<String> teams = List.of("Bayern München");
        System.out.println(sd.findGameIdsForTeams(teams));

    }


}
