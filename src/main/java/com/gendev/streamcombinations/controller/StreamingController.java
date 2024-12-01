package com.gendev.streamcombinations.controller;

import com.gendev.streamcombinations.model.*;
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
    public List<StreamingPackage> getCheapestPackages(@RequestParam(required = false) Set<String> teams,
                                              @RequestParam(required = false) Set<String> tournaments,
                                              @RequestParam(required = false) String startDate,
                                              @RequestParam(required = false) String endDate) {
        Set<Game> games = streamingService.getRequiredGames(teams, tournaments, DateUtils.parse(startDate), DateUtils.parse(endDate));
        return streamingService.findCheapestCombination(games);
    }

    @GetMapping("/least-packages")
    public List<StreamingPackage> getLeastPackages(@RequestParam(required = false) Set<String> teams,
                                              @RequestParam(required = false) Set<String> tournaments,
                                              @RequestParam(required = false) String startDate,
                                              @RequestParam(required = false) String endDate) {
        Set<Game> games = streamingService.getRequiredGames(teams, tournaments, DateUtils.parse(startDate), DateUtils.parse(endDate));
        return streamingService.findLeastServicesCombination(games);
    }


}
