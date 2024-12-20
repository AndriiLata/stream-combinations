package com.gendev.streamcombinations.model.main;


import lombok.Getter;

@Getter
public class TeamCountry {
    private final String team;
    private final String country;

    public TeamCountry(String team, String country) {
        this.team = team;
        this.country = country;
    }

}
