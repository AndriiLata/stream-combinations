package com.gendev.streamcombinations.util;

import com.gendev.streamcombinations.model.main.TeamCountry;
import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.main.StreamingOffer;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Getter
@Service
public class FetchData {
    private final List<Game> games;
    private final List<StreamingOffer> streamingOffers;
    private final List<StreamingPackage> streamingPackages;
    private final Set<TeamCountry> teamCountries;


    public FetchData() {
        CSVLoader loader = new CSVLoader();
        this.games = loader.loadGamesFromCSV("dataset/bc_game.csv");
        this.streamingOffers = loader.loadStreamingOfferFromCSV("dataset/bc_streaming_offer.csv");
        this.streamingPackages = loader.loadStreamingPackageFromCSV("dataset/bc_streaming_package.csv");
        this.teamCountries = loader.loadTeamCountryFromCSV("dataset/teams_countries.csv");
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
