package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.response.CountryTeamTournament;
import com.gendev.streamcombinations.util.FetchData;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import com.gendev.streamcombinations.indexer.*;
import com.gendev.streamcombinations.model.*;

@Service
public class SearchInputService {

        private final GameFetch gameFetch;
        private final FetchData fetchData;

        @Autowired
        public SearchInputService(GameFetch gameFetch, FetchData fetchData) {
            this.gameFetch = gameFetch;
            this.fetchData = fetchData;
        }

        public Set<CountryTeamTournament> getCountryTeamTournaments() {
            Set<String> tournaments = gameFetch.getAllTournaments();
            Set<TeamCountry> teamCountries = fetchData.getTeamCountries();

            Map<String, Set<String>> countryToTeamsMap = new HashMap<>();

            for (TeamCountry tc : teamCountries) {
                String country = tc.getCountry();
                String team = tc.getTeam();
                countryToTeamsMap.computeIfAbsent(country, k -> new HashSet<>()).add(team);
            }

            Set<CountryTeamTournament> result = new HashSet<>();

            // For each country, collect the tournaments its teams have participated in
            for (Map.Entry<String, Set<String>> entry : countryToTeamsMap.entrySet()) {
                String country = entry.getKey();
                Set<String> teams = entry.getValue();
                Set<String> countryTournaments = new HashSet<>();

                // For each team, retrieve games and extract tournaments
                for (String team : teams) {
                    Set<Game> games = gameFetch.getGamesByTeam(team);

                    for (Game game : games) {
                        String tournament = game.getTournament_name();
                        if (tournament != null && tournaments.contains(tournament)) {
                            countryTournaments.add(tournament);
                        }
                    }
                }

                // Create and populate the CountryTeamTournament object
                CountryTeamTournament ctt = new CountryTeamTournament(country, teams, countryTournaments);

                result.add(ctt);
            }

            return result;

        }

}
