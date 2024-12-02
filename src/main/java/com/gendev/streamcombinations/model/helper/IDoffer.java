package com.gendev.streamcombinations.model.helper;

import lombok.Getter;

@Getter
public class IDoffer {
    private boolean live;
    private boolean highlights;

    public IDoffer(boolean live, boolean highlights) {
        this.live = live;
        this.highlights = highlights;
    }
}
