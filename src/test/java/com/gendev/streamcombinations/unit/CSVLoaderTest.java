package com.gendev.streamcombinations.unit;

import com.gendev.streamcombinations.model.Game;
import com.gendev.streamcombinations.model.StreamingOffer;
import com.gendev.streamcombinations.model.StreamingPackage;
import com.gendev.streamcombinations.util.CSVLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CSVLoaderTest {
    String filepathGames = "/Users/andriilata/Desktop/GenDev24/bc_game.csv";
    String filepathStreamingOffer = "/Users/andriilata/Desktop/GenDev24/bc_streaming_offer.csv";
    String filepathStreamingPackage = "/Users/andriilata/Desktop/GenDev24/bc_streaming_package.csv";

    static CSVLoader loader = new CSVLoader();


    @Test
    void loadingGames() {
        List<Game> gamesExpected = loader.loadGamesFromCSV(filepathGames);
        assertEquals(8871, gamesExpected.size());
    }

    @Test
    void gameContent() {
        List<Game> gamesExpected = loader.loadGamesFromCSV(filepathGames);
        assertEquals(1, gamesExpected.get(0).getId());
        assertEquals("Deutschland", gamesExpected.get(0).getTeam_home());
        assertEquals("Schottland", gamesExpected.get(0).getTeam_away());
        assertEquals("2024-06-14 19:00:00", gamesExpected.get(0).getStarts_at());
        assertEquals("Europameisterschaft 2024", gamesExpected.get(0).getTournament_name());
    }

    @Test
    void loadingStreamingOffers() {
        List<StreamingOffer> streamingOffers = loader.loadStreamingOfferFromCSV(filepathStreamingOffer);
        assertEquals(32439, streamingOffers.size());
    }

    @Test
    void streamingOfferContent() {
        List<StreamingOffer> streamingOffers = loader.loadStreamingOfferFromCSV(filepathStreamingOffer);
        assertEquals(1, streamingOffers.get(0).getId());
        assertEquals(2, streamingOffers.get(0).getStreaming_package_id());
        assertEquals(1, streamingOffers.get(0).getLive());
        assertEquals(1, streamingOffers.get(0).getHighlights());
    }

    @Test
    void loadingStreamingPackages() {
        List<StreamingPackage> streamingPackages = loader.loadStreamingPackageFromCSV(filepathStreamingPackage);
        assertEquals(37, streamingPackages.size());
    }

    @Test
    void streamingPackageContent() {
        List<StreamingPackage> streamingPackages = loader.loadStreamingPackageFromCSV(filepathStreamingPackage);
        assertEquals(2, streamingPackages.get(0).getId());
        assertEquals("MagentaTV - MegaSport", streamingPackages.get(0).getName());
        assertEquals(7777, streamingPackages.get(0).getMonthly_price_cents());
        assertEquals(6000, streamingPackages.get(0).getMonthly_price_yearly_subscription_in_cents());
    }
}
