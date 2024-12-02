package com.gendev.streamcombinations.controller;

import com.gendev.streamcombinations.indexer.GameFetch;
import com.gendev.streamcombinations.model.response.CountryTeamTournament;
import com.gendev.streamcombinations.service.SearchInputService;
import com.gendev.streamcombinations.service.StreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/search")
public class SearchController {
    //ToDo: Implement api for showing games, tournaments to search

    //ToDo: Show all teams and tournaments based on location (All, America, Europe, etc)
    @Autowired
    private GameFetch gameFetch;
    @Autowired
    private SearchInputService searchInputService;

    @RequestMapping("/all-tournaments")
    public Set<String> getTournaments() {
        return gameFetch.getAllTournaments();
    }

    @RequestMapping("/all-teams")
    public Set<String> getTeams() {
        return gameFetch.getAllTeams();
    }

    @RequestMapping("/country-team-tournaments")
    public Set<CountryTeamTournament> getCountryTeamTournaments() {
        return searchInputService.getCountryTeamTournaments();
    }
}
