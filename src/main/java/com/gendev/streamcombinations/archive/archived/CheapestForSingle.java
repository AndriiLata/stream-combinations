package com.gendev.streamcombinations.archive.archived;


import com.gendev.streamcombinations.model.Game;
import com.gendev.streamcombinations.model.StreamingOffer;
import com.gendev.streamcombinations.model.StreamingPackage;
import com.gendev.streamcombinations.util.FetchData;

import java.util.*;

// here I want to find a list of the cheapest services for a single team.
// In the end I want to have a hashmap which has -> Sorted array of services by price per Team -> <SortedList, Team>
public class CheapestForSingle {

    private Map<String, List<StreamingPackage>> servicesForTeam;
    private List<Game> games;
    private List<StreamingOffer> sOffers;
    private List<StreamingPackage> sPackeges;

    public CheapestForSingle(){
        this.servicesForTeam = new HashMap<>();

        FetchData fd = new FetchData();
        this.games = fd.getGames();
        this.sOffers = fd.getStreamingOffers();
        this.sPackeges = fd.getStreamingPackages();
    }

    //Returns all gameIDs for a specific team
    public Set<Integer> findGameIdsForTeam(String team){
        Set<Integer> gameIDs = new HashSet<>();
        for(Game game: this.games){
            if(game.getTeam_home().equals(team) || game.getTeam_away().equals(team)){
                gameIDs.add(game.getId());
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
    public Set<Integer> intersectionIDs(String team, int packageID){
        Set<Integer> gameIDsTeam = findGameIdsForTeam(team);
        Set<Integer> gameIDsPackage = findGameIdsForPackage(packageID);

        Set<Integer> intersectionIDs = new HashSet<>(gameIDsPackage);

        intersectionIDs.retainAll(gameIDsTeam);

        return intersectionIDs;
    }

    //We search with the team name and get a map with the packageID and the gameIDs that are covered
    public Map<Integer, Set<Integer>> gamePackagesForTeam(String team){
        Map<Integer, Set<Integer>> gpft = new HashMap<>();

        for(StreamingPackage sp: this.sPackeges){
            Set<Integer> gameIDs = intersectionIDs(team, sp.getId());
            if(!gameIDs.isEmpty()){
                gpft.put(sp.getId(), gameIDs);
            }
        }

        return gpft;
    }



}
