package com.gendev.streamcombinations.model.response;

import lombok.Getter;

import java.util.Set;

@Getter
public class CountryTeamTournament {

    private final String country;
    private final Set<String> teams;
    private final Set<String> tournaments;

    public CountryTeamTournament(String country, Set<String> teams, Set<String> tournaments) {
        this.country = country;
        this.teams = teams;
        this.tournaments = tournaments;
    }
}
