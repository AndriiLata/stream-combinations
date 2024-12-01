package com.gendev.streamcombinations.model;

import lombok.Getter;

@Getter
public class GameOffer {

    private Game game;
    private boolean live;
    private boolean highlights;

    public GameOffer(Game game, boolean live, boolean highlights) {
        this.game = game;
        this.live = live;
        this.highlights = highlights;
    }

}
