package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.model.Game;
import com.gendev.streamcombinations.util.CSVLoader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandleGames {
    private List<Game> games;

    public HandleGames() {
        CSVLoader loader = new CSVLoader();
        this.games = loader.loadGamesFromCSV("/Users/andriilata/Desktop/GenDev24");
    }

    public List<Game> getGames() {
        return games;
    }

    public void printGames() {
        if (games != null && !games.isEmpty()) {
            for (Game game : games) {
                System.out.println(game.getTeam_away() + " vs " + game.getTeam_home() + " on " + game.getStarts_at());
            }
        } else {
            System.out.println("No games found!");
        }
    }
}
