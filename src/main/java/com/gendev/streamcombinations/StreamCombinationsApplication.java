package com.gendev.streamcombinations;

import com.gendev.streamcombinations.indexer.GameFetch;
import com.gendev.streamcombinations.indexer.StreamingOfferFetch;
import com.gendev.streamcombinations.indexer.StreamingPackageFetch;
import com.gendev.streamcombinations.model.Game;
import com.gendev.streamcombinations.service.StreamingService;
import com.gendev.streamcombinations.util.DateUtils;
import com.gendev.streamcombinations.util.FetchData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.Set;

@SpringBootApplication
public class StreamCombinationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamCombinationsApplication.class, args);
        FetchData fetchData = new FetchData();
        GameFetch gameFetch = new GameFetch(fetchData.getGames());
        StreamingOfferFetch streamingOfferFetch = new StreamingOfferFetch(fetchData.getStreamingOffers());
        StreamingPackageFetch streamingPackageFetch = new StreamingPackageFetch(fetchData.getStreamingPackages());
        StreamingService sS = new StreamingService(gameFetch, streamingOfferFetch, streamingPackageFetch);

        Set<String> teams = Set.of("Deutschland", "Albanien");
        Set<String> tournaments = Set.of("Bundesliga 23/24");
        LocalDateTime startDate = DateUtils.parse("2023-08-27 15:00:00");
        LocalDateTime endDate = DateUtils.parse("2023-08-27 17:00:00");

        Set<Game> games = sS.getRequiredGames(teams, tournaments, startDate, endDate);

        for (Game game : games) {
            System.out.println(game.getId() + " " + game.getTeam_home() + " vs " + game.getTeam_away() + " on " + game.getStarts_at());
        }


    }

}
