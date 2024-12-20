package com.gendev.streamcombinations.controller;

import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import com.gendev.streamcombinations.model.response.SearchResult;
import com.gendev.streamcombinations.service.StreamingService;
import com.gendev.streamcombinations.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @GetMapping("/best-packages")
    public SearchResult getBestPackages(@RequestParam(required = false) Set<String> teams,
                                        @RequestParam(required = false) Set<String> tournaments,
                                        @RequestParam(required = false) String startDate,
                                        @RequestParam(required = false) String endDate) {
        return streamingService.getBestPackagesResult(teams, tournaments, startDate, endDate);
    }

    @GetMapping("/packages-by-ids")
    public List<StreamingPackage> getPackagesByIds(@RequestParam List<Integer> ids) {
        return streamingService.getPackagesByIds(ids);
    }
}
