package com.gendev.streamcombinations.util;

import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.main.StreamingOffer;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import com.gendev.streamcombinations.model.main.TeamCountry;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.InputStream;
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

        // Load the CSV files as InputStreams from the classpath
        InputStream gamesStream = getResourceStream("/dataset/bc_game.csv");
        InputStream offersStream = getResourceStream("/dataset/bc_streaming_offer.csv");
        InputStream packagesStream = getResourceStream("/dataset/bc_streaming_package.csv");
        InputStream teamsCountriesStream = getResourceStream("/dataset/teams_countries.csv");

        this.games = loader.loadGamesFromCSV(gamesStream);
        this.streamingOffers = loader.loadStreamingOfferFromCSV(offersStream);
        this.streamingPackages = loader.loadStreamingPackageFromCSV(packagesStream);
        this.teamCountries = loader.loadTeamCountryFromCSV(teamsCountriesStream);
    }

    private InputStream getResourceStream(String resourcePath) {
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new RuntimeException("Could not find resource: " + resourcePath);
        }
        return is;
    }
}
