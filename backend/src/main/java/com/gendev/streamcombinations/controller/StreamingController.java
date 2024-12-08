package com.gendev.streamcombinations.controller;

import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.helper.GameOffer;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import com.gendev.streamcombinations.model.response.SearchResult;
import com.gendev.streamcombinations.service.*;
import com.gendev.streamcombinations.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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


    @GetMapping("/cheapest-packages")
    public SearchResult getCheapestPackages(@RequestParam(required = false) Set<String> teams,
                                            @RequestParam(required = false) Set<String> tournaments,
                                            @RequestParam(required = false) String startDate,
                                            @RequestParam(required = false) String endDate) {
        LocalDateTime start = DateUtils.parse(startDate);
        LocalDateTime end = DateUtils.parse(endDate);

        Set<Game> games = streamingService.getRequiredGames(teams, tournaments, start, end);

        //Mein neues Feature für die Monate berechnung
        int monthsDifference = -1;
        if (start != null && end != null) {
            monthsDifference = DateUtils.monthsBetween(start, end);
            if (monthsDifference < 0) {
                monthsDifference = -1;
            }
        }

        List<StreamingPackage> streamingPackages = streamingService.findCheapestCombination(games, monthsDifference);
        Map<StreamingPackage, Set<GameOffer>> packageToGameOffers = streamingService.buildPackageToGameOffers(games);
        // Rank other packages
        List<StreamingPackage> otherPackages = streamingService.rankOtherPackages(games, packageToGameOffers, streamingPackages, monthsDifference);

        // It fixed my frontend null pointer exception
        for (StreamingPackage pkg : streamingPackages) {
            packageToGameOffers.putIfAbsent(pkg, Collections.emptySet());
        }

        for (StreamingPackage pkg : otherPackages) {
            packageToGameOffers.putIfAbsent(pkg, Collections.emptySet());
        }

        return new SearchResult(games, streamingPackages, packageToGameOffers, otherPackages);
    }

    @GetMapping("/packages-by-ids")
    public List<StreamingPackage> getPackagesByIds(@RequestParam List<Integer> ids) {
        return streamingService.getPackagesByIds(ids);
    }



}
