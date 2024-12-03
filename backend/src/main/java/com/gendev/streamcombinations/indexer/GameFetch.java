package com.gendev.streamcombinations.indexer;
import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.util.FetchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class GameFetch {

    private final Map<Integer, Game> gamesById = new HashMap<>();
    private final Map<String, Set<Game>> gamesByTeam = new HashMap<>();
    private final Map<String, Set<Game>> gamesByTournament = new HashMap<>();
    private final NavigableMap<LocalDateTime, Set<Game>> gamesByDate = new TreeMap<>();

    @Autowired
    public GameFetch(FetchData fetchData) {
        List<Game> gamesList = fetchData.getGames();
        if(gamesList != null) {
            for (Game game : gamesList) {
                gamesById.put(game.getId(), game);

                // Index by team
                gamesByTeam.computeIfAbsent(game.getTeam_home(), k -> new HashSet<>()).add(game);
                gamesByTeam.computeIfAbsent(game.getTeam_away(), k -> new HashSet<>()).add(game);

                // Index by tournament
                gamesByTournament.computeIfAbsent(game.getTournament_name(), k -> new HashSet<>()).add(game);

                // Index by date
                gamesByDate.computeIfAbsent(game.getStarts_at(), k -> new HashSet<>()).add(game);
            }
        } else {
            System.out.println("No games found! Cant read your CSV file");
        }
    }

    // Methods to retrieve games based on criteria
    public Set<Game> getGamesByTeam(String team) {
        return gamesByTeam.getOrDefault(team, Collections.emptySet());
    }

    public Set<Game> getGamesByTournament(String tournament) {
        return gamesByTournament.getOrDefault(tournament, Collections.emptySet());
    }

    public Set<Game> getGamesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        NavigableMap<LocalDateTime, Set<Game>> subMap = gamesByDate.subMap(startDate, true, endDate, true);
        Set<Game> result = new HashSet<>();
        for (Set<Game> gameSet : subMap.values()) {
            result.addAll(gameSet);
        }
        return result;
    }

    public Game getGameById(int id) {
        return gamesById.get(id);
    }

    public Set<String> getAllTournaments() {
        return new HashSet<>(gamesByTournament.keySet());
    }

    public Set<String> getAllTeams() {
        return new HashSet<>(gamesByTeam.keySet());
    }


}
