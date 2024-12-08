package com.gendev.streamcombinations.model.response;

import com.gendev.streamcombinations.model.helper.TournamentDateRange;
import lombok.Getter;

import java.util.Set;

@Getter
public class CountryTeamTournament {

    private final String country;
    private final Set<String> teams;
    private final Set<TournamentDateRange> tournaments;

    public CountryTeamTournament(String country, Set<String> teams, Set<TournamentDateRange> tournaments) {
        this.country = country;
        this.teams = teams;
        this.tournaments = tournaments;
    }
}
