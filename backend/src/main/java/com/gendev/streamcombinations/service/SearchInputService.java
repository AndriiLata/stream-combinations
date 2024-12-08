package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.model.helper.TournamentDateRange;
import com.gendev.streamcombinations.model.main.TeamCountry;
import com.gendev.streamcombinations.model.main.Game;
import com.gendev.streamcombinations.model.response.CountryTeamTournament;
import com.gendev.streamcombinations.util.FetchData;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;
import com.gendev.streamcombinations.indexer.*;
import java.time.LocalDateTime;

@Service
public class SearchInputService {

    private final GameFetch gameFetch;
    private final FetchData fetchData;

    @Autowired
    public SearchInputService(GameFetch gameFetch, FetchData fetchData) {
        this.gameFetch = gameFetch;
        this.fetchData = fetchData;
    }

    public List<CountryTeamTournament> getCountryTeamTournaments() {
        Set<String> tournaments = gameFetch.getAllTournaments();
        Map<String, Set<String>> countryToTeamsMap = buildCountryToTeamsMap(fetchData.getTeamCountries());
        List<CountryTeamTournament> result = new ArrayList<>();
        Map<CountryTeamTournament, Integer> totalGamesMap = new HashMap<>();

        for (Map.Entry<String, Set<String>> entry : countryToTeamsMap.entrySet()) {
            String country = entry.getKey();
            Set<String> teams = entry.getValue();
            Map<String, TournamentRangeTracker> trackers = new HashMap<>();
            int totalGames = 0;

            for (String team : teams) {
                Set<Game> games = gameFetch.getGamesByTeam(team);
                totalGames += games.size();
                for (Game g : games) {
                    String t = g.getTournament_name();
                    if (t != null && tournaments.contains(t))
                        trackers.computeIfAbsent(t, k -> new TournamentRangeTracker()).update(g.getStarts_at());
                }
            }

            Set<TournamentDateRange> countryTournaments = trackers.entrySet().stream()
                    .map(e -> new TournamentDateRange(e.getKey(), e.getValue().earliest, e.getValue().latest))
                    .collect(Collectors.toSet());

            CountryTeamTournament ctt = new CountryTeamTournament(country, teams, countryTournaments);
            result.add(ctt);
            totalGamesMap.put(ctt, totalGames);
        }

        result.sort((ctt1, ctt2) -> Integer.compare(totalGamesMap.get(ctt2), totalGamesMap.get(ctt1)));
        return result;
    }

    private Map<String, Set<String>> buildCountryToTeamsMap(Set<TeamCountry> teamCountries) {
        Map<String, Set<String>> map = new HashMap<>();
        for (TeamCountry tc : teamCountries)
            map.computeIfAbsent(tc.getCountry(), k -> new HashSet<>()).add(tc.getTeam());
        return map;
    }

    private static class TournamentRangeTracker {
        LocalDateTime earliest;
        LocalDateTime latest;

        void update(LocalDateTime date) {
            if (earliest == null || date.isBefore(earliest)) earliest = date;
            if (latest == null || date.isAfter(latest)) latest = date;
        }
    }
}
