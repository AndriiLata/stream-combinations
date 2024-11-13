package com.gendev.streamcombinations.util;

import com.gendev.streamcombinations.model.Game;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVLoader {

    public List<Game> loadGamesFromCSV(String filePath) {
        List<Game> games = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            reader.readNext(); // skip header

            while ((line = reader.readNext()) != null) {
                Game game = new Game(Long.parseLong(line[0]), line[1], line[2], line[3], line[4]);

                games.add(game);
            }


        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return games;
    }
}
