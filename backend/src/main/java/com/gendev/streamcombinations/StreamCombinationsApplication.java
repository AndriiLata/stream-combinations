package com.gendev.streamcombinations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StreamCombinationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamCombinationsApplication.class, args);

    }

}


//        FetchData fetchData = new FetchData();
//        GameFetch gameFetch = new GameFetch(fetchData.getGames());
//        StreamingOfferFetch streamingOfferFetch = new StreamingOfferFetch(fetchData.getStreamingOffers());
//        StreamingPackageFetch streamingPackageFetch = new StreamingPackageFetch(fetchData.getStreamingPackages());
//        StreamingService sS = new StreamingService(gameFetch, streamingOfferFetch, streamingPackageFetch);
//
//        Set<String> teams = Set.of("Schweiz");
//       Set<String> tournaments = Set.of("UEFA Champions League 24/25");
////        LocalDateTime startDate = DateUtils.parse("2023-08-27 15:00:00");
////        LocalDateTime endDate = DateUtils.parse("2023-08-27 17:00:00");
//
//        Set<Game> games = sS.getRequiredGames(teams, null, null, null);
//
//        List<StreamingPackage> streamingPackages = sS.findCheapestCombination(games);
//        List<StreamingPackage> streamingPackages = sS.findLeastServicesCombination(games);
//
//        Map<StreamingPackage, Set<GameOffer>> btg = sS.buildPackageToGameOffers(games);
//
//
//
//        for (StreamingPackage streamingPackage : streamingPackages) {
//            System.out.println("Streaming package: " + streamingPackage.getName());
//            System.out.println(btg.get(streamingPackage).size());
//            btg.get(streamingPackage).forEach(gameOffer -> {
//                System.out.println("Game: " + gameOffer.getGame().getId()+ " with live: " + gameOffer.isLive() + " and highlights: " + gameOffer.isHighlights());
////                System.out.println("Game: " + gameOffer.getGame().getId());
//            });
//        }

