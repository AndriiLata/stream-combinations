package com.gendev.streamcombinations.util;

import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.main.StreamingOffer;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import com.gendev.streamcombinations.model.main.TeamCountry;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class CSVLoader {

    public List<Game> loadGamesFromCSV(InputStream inputStream) {
        List<Game> games = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] line;
            reader.readNext(); // skip header
            while ((line = reader.readNext()) != null) {
                Game game = new Game(
                        Integer.parseInt(line[0]),
                        line[1],
                        line[2],
                        DateUtils.parse(line[3]),
                        line[4]);
                games.add(game);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error reading games CSV", e);
        }
        return games;
    }

    public List<StreamingPackage> loadStreamingPackageFromCSV(InputStream inputStream) {
        List<StreamingPackage> streamingPackages = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] line;
            reader.readNext(); // skip header
            while ((line = reader.readNext()) != null) {
                int parsedValue = Objects.equals(line[2], "") ? 7777 : Integer.parseInt(line[2]);
                StreamingPackage streamingPackage = new StreamingPackage(
                        Integer.parseInt(line[0]),
                        line[1],
                        parsedValue,
                        Integer.parseInt(line[3]));
                streamingPackages.add(streamingPackage);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error reading streaming packages CSV", e);
        }
        return streamingPackages;
    }

    public List<StreamingOffer> loadStreamingOfferFromCSV(InputStream inputStream) {
        List<StreamingOffer> streamingOffers = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] line;
            reader.readNext(); // skip header
            while ((line = reader.readNext()) != null) {
                StreamingOffer streamingOffer = new StreamingOffer(
                        Integer.parseInt(line[0]),
                        Integer.parseInt(line[1]),
                        Integer.parseInt(line[2]),
                        Integer.parseInt(line[3]));
                streamingOffers.add(streamingOffer);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error reading streaming offers CSV", e);
        }
        return streamingOffers;
    }

    public Set<TeamCountry> loadTeamCountryFromCSV(InputStream inputStream) {
        Set<TeamCountry> teamCountries = new HashSet<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] line;
            reader.readNext(); // skip header
            while ((line = reader.readNext()) != null) {
                TeamCountry teamCountry = new TeamCountry(line[0], line[1]);
                teamCountries.add(teamCountry);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error reading team countries CSV", e);
        }
        return teamCountries;
    }
}
