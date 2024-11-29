package com.gendev.streamcombinations.service;
import com.gendev.streamcombinations.indexer.*;
import com.gendev.streamcombinations.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


public class StreamingService {
    private GameFetch gameFetch;
    private StreamingOfferFetch streamingOfferFetch;
    private StreamingPackageFetch streamingPackageFetch;


    public StreamingService(GameFetch gameFetch, StreamingOfferFetch streamingOfferFetch, StreamingPackageFetch streamingPackageFetch) {
        this.gameFetch = gameFetch;
        this.streamingOfferFetch = streamingOfferFetch;
        this.streamingPackageFetch = streamingPackageFetch;
    }

    public Set<Game> getRequiredGames(Set<String> teams, Set<String> tournaments, LocalDateTime startDate, LocalDateTime endDate) {
        Set<Game> requiredGames = new HashSet<>();

        // Get games by teams
        if(teams != null) {
            for (String team : teams) {
                requiredGames.addAll(gameFetch.getGamesByTeam(team));
            }
        }

        // Get games by tournaments
        if(tournaments != null) {
            for (String tournament : tournaments) {
                requiredGames.addAll(gameFetch.getGamesByTournament(tournament));
            }
        }

        // Get games by date range
        if (startDate != null && endDate != null) {
            requiredGames.removeIf(game -> game.getStarts_at().isBefore(startDate) || game.getStarts_at().isAfter(endDate));
        }

        return requiredGames;
    }

    public List<StreamingPackage> findCheapestCombination(Set<Game> requiredGames){
        // ToDo
        return null;
    }

    public List<StreamingPackage> findLeastServicesCombination(Set<Game> requiredGames){
        // ToDo
        return null;
    }
}
