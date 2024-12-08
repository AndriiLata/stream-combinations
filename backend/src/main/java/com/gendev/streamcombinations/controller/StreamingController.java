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


    @GetMapping("/cheapest-packages")
    public SearchResult getCheapestPackages(@RequestParam(required = false) Set<String> teams,
                                            @RequestParam(required = false) Set<String> tournaments,
                                            @RequestParam(required = false) String startDate,
                                            @RequestParam(required = false) String endDate) {
        Set<Game> games = streamingService.getRequiredGames(teams, tournaments, DateUtils.parse(startDate), DateUtils.parse(endDate));
        List<StreamingPackage> streamingPackages = streamingService.findCheapestCombination(games);
        Map<StreamingPackage, Set<GameOffer>> packageToGameOffers = streamingService.buildPackageToGameOffers(games);

        // Rank other packages
        List<StreamingPackage> otherPackages = streamingService.rankOtherPackages(games, packageToGameOffers, streamingPackages);

        // Ensure every package (both chosen and other) is represented in packageToGameOffers
        // If a package doesn't cover any games, we still want an empty set to avoid undefined issues.
        for (StreamingPackage pkg : streamingPackages) {
            packageToGameOffers.putIfAbsent(pkg, Collections.emptySet());
        }

        for (StreamingPackage pkg : otherPackages) {
            packageToGameOffers.putIfAbsent(pkg, Collections.emptySet());
        }

        return new SearchResult(games, streamingPackages, packageToGameOffers, otherPackages);
    }


}
