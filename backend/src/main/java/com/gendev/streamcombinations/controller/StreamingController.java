package com.gendev.streamcombinations.controller;

import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.helper.GameOffer;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import com.gendev.streamcombinations.model.response.SearchResult;
import com.gendev.streamcombinations.service.*;
import com.gendev.streamcombinations.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/streaming")
public class StreamingController {

    @Autowired
    private StreamingService streamingService;

    @GetMapping("/games")
    public Set<Game> getGames(@RequestParam(required = false) Set<String> teams,
                              @RequestParam(required = false) Set<String> tournaments,
                              @RequestParam(required = false) String startDate,
                              @RequestParam(required = false) String endDate) {
        return streamingService.getRequiredGames(teams, tournaments, DateUtils.parse(startDate), DateUtils.parse(endDate));
    }


    @GetMapping("/least-packages")
    public SearchResult getLeastPackages(@RequestParam(required = false) Set<String> teams,
                                              @RequestParam(required = false) Set<String> tournaments,
                                              @RequestParam(required = false) String startDate,
                                              @RequestParam(required = false) String endDate) {
        Set<Game> games = streamingService.getRequiredGames(teams, tournaments, DateUtils.parse(startDate), DateUtils.parse(endDate));
        List<StreamingPackage> streamingPackages = streamingService.findLeastServicesCombination(games);
        Map<StreamingPackage, Set<GameOffer>> packageToGameOffers = streamingService.buildPackageToGameOffers(games);
        return new SearchResult(games, streamingPackages, packageToGameOffers);
    }

    @GetMapping("/cheapest-packages")
    public SearchResult getCheapestPackages(@RequestParam(required = false) Set<String> teams,
                                            @RequestParam(required = false) Set<String> tournaments,
                                            @RequestParam(required = false) String startDate,
                                            @RequestParam(required = false) String endDate) {
        Set<Game> games = streamingService.getRequiredGames(teams, tournaments, DateUtils.parse(startDate), DateUtils.parse(endDate));
        List<StreamingPackage> streamingPackages = streamingService.findCheapestCombination(games);
        Map<StreamingPackage, Set<GameOffer>> packageToGameOffers = streamingService.buildPackageToGameOffers(games);


        return new SearchResult(games, streamingPackages, packageToGameOffers);
    }

}
