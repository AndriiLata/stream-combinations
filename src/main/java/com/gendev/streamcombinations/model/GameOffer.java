package com.gendev.streamcombinations.model;

import lombok.Getter;

@Getter
public class GameOffer {

    private Game game;
    private boolean live;
    private boolean highlights;

    public GameOffer(Game game, int live, int highlights) {
        this.game = game;
        this.live = live == 1;
        this.highlights = highlights == 1;
    }

}
