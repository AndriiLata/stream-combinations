package com.gendev.streamcombinations.model.main;

import lombok.Getter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Getter
@Entity
public class StreamingOffer {

    @Id
    private int id;

    private int streaming_package_id;
    private boolean live;
    private boolean highlights;

    public StreamingOffer() {
    }

    public StreamingOffer(int id, int streaming_package_id, int live, int highlights) {
        this.id = id;
        this.streaming_package_id = streaming_package_id;
        this.live = live == 1;
        this.highlights = highlights == 1;
    }

}
