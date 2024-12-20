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

}
