package com.gendev.streamcombinations.model.helper;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TournamentDateRange {
    private final String tournament;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public TournamentDateRange(String tournament, LocalDateTime startDate, LocalDateTime endDate) {
        this.tournament = tournament;
        this.startDate = startDate;
        this.endDate = endDate;
    }


}
