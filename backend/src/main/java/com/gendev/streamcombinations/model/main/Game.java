package com.gendev.streamcombinations.model.main;

import lombok.Getter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
public class Game {

    @Id
    private int id;

    private String team_home;
    private String team_away;
    private LocalDateTime starts_at;
    private String tournament_name;

    public Game() {
    }

    public Game(int id, String team_home, String team_away, LocalDateTime starts_at, String tournament_name) {
        this.id = id;
        this.team_home = team_home;
        this.team_away = team_away;
        this.starts_at = starts_at;
        this.tournament_name = tournament_name;
    }



}
