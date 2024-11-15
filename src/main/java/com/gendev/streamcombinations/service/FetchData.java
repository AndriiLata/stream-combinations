package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.model.Game;
import com.gendev.streamcombinations.model.StreamingOffer;
import com.gendev.streamcombinations.model.StreamingPackage;
import com.gendev.streamcombinations.util.CSVLoader;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

//ToDo: Test if this works

@Getter
@Service
public class FetchData {
    private final List<Game> games;
    private final List<StreamingOffer> streamingOffers;
    private final List<StreamingPackage> streamingPackages;


    public FetchData() {
        CSVLoader loader = new CSVLoader();
        this.games = loader.loadGamesFromCSV("/Users/andriilata/Desktop/GenDev24/bc_game.csv");
        this.streamingOffers = loader.loadStreamingOfferFromCSV("/Users/andriilata/Desktop/GenDev24/bc_streaming_offer.csv");
        this.streamingPackages = loader.loadStreamingPackageFromCSV("/Users/andriilata/Desktop/GenDev24/bc_streaming_package.csv");
    }




    //THIS IS JUST FOR TESTING
//    public void printGames() {
//        if (games != null && !games.isEmpty()) {
//            for (Game game : games) {
//                System.out.println(game.getTeam_away() + " vs " + game.getTeam_home() + " on " + game.getStarts_at());
//            }
//        } else {
//            System.out.println("No games found!");
//        }
//    }
//    //print streaming offers
//    public void printStreamingOffers() {
//        if (streamingOffers != null && !streamingOffers.isEmpty()) {
//            for (StreamingOffer streamingOffer : streamingOffers) {
//                System.out.println("Streaming offer: " + streamingOffer.getStreaming_package_id() + " with " + streamingOffer.getLive() + " live and " + streamingOffer.getHighlights() + " highlights");
//            }
//        } else {
//            System.out.println("No streaming offers found!");
//        }
//    }
//    //print streaming packages
//    public void printStreamingPackages() {
//        if (streamingPackages != null && !streamingPackages.isEmpty()) {
//            for (StreamingPackage streamingPackage : streamingPackages) {
//                System.out.println("Streaming package: " + streamingPackage.getName() + " with monthly price " + streamingPackage.getMonthly_price_cents() + " and yearly subscription price " + streamingPackage.getMonthly_price_yearly_subscription_in_cents());
//            }
//        } else {
//            System.out.println("No streaming packages found!");
//        }
//    }
}
