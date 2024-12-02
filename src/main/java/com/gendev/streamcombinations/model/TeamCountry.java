package com.gendev.streamcombinations.model;

import jakarta.persistence.Entity;


public class TeamCountry {
    private final String team;
    private final String country;

    public TeamCountry(String team, String country) {
        this.team = team;
        this.country = country;
    }

    public String getTeam() {
        return team;
    }

    public String getCountry() {
        return country;
    }
}
