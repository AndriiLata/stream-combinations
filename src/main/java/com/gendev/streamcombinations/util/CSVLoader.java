package com.gendev.streamcombinations.util;

import com.gendev.streamcombinations.model.Game;
import com.gendev.streamcombinations.model.StreamingOffer;
import com.gendev.streamcombinations.model.StreamingPackage;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CSVLoader {
    // Load games from CSV file
    public List<Game> loadGamesFromCSV(String filePath) {
        List<Game> games = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            reader.readNext(); // skip header
            while ((line = reader.readNext()) != null) {
                Game game = new Game(Integer.parseInt(line[0]), line[1], line[2], DateUtils.parse(line[3]), line[4]);

                games.add(game);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return games;
    }
    // load streaming packages from CSV file
    public List<StreamingPackage> loadStreamingPackageFromCSV(String filePath){
        List<StreamingPackage> streamingPackages = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            reader.readNext(); // skip header
            while ((line = reader.readNext()) != null) {
                int parsedValue = Objects.equals(line[2], "") ? 7777 : Integer.parseInt(line[2]); //ToDo: Definetly need to change this

                StreamingPackage streamingPackage = new StreamingPackage(Integer.parseInt(line[0]), line[1], parsedValue, Integer.parseInt(line[3]));

                streamingPackages.add(streamingPackage);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return streamingPackages;
    }
    // load streaming offers from CSV file
    public List<StreamingOffer> loadStreamingOfferFromCSV(String filePath){
        List<StreamingOffer> streamingOffers = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            reader.readNext(); // skip header
            while ((line = reader.readNext()) != null) {
                StreamingOffer streamingOffer = new StreamingOffer(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3]));

                streamingOffers.add(streamingOffer);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return streamingOffers;
    }

// JUST FOR TESTING
//    public static void main(String[] args) {
//        CSVLoader loader = new CSVLoader();
//        List<Game> games = loader.loadGamesFromCSV("/Users/andriilata/Desktop/GenDev24/bc_game.csv");
//        for (Game game : games) {
//            System.out.println(game.getId() +" "+ game.getTeam_away() + " " + game.getTeam_home() + " " + game.getStarts_at() + " " + game.getTournament_name());
//        }

//        List<StreamingPackage> streamingPackages = loader.loadStreamingPackageFromCSV("/Users/andriilata/Desktop/GenDev24/bc_streaming_package.csv");
//        for (StreamingPackage streamingPackage : streamingPackages) {
//            System.out.println(streamingPackage.getId() + " " + streamingPackage.getName() + " " + streamingPackage.getMonthly_price_cents() + " " + streamingPackage.getMonthly_price_yearly_subscription_in_cents());
//        }

//        List<StreamingOffer> streamingOffers = loader.loadStreamingOfferFromCSV("/Users/andriilata/Desktop/GenDev24/bc_streaming_offer.csv");
//        for (StreamingOffer streamingOffer : streamingOffers) {
//            System.out.println(streamingOffer.getId() +" "+ streamingOffer.getStreaming_package_id() + " " + streamingOffer.getHighlights() + " " + streamingOffer.getLive());
//        }
//    }

}
