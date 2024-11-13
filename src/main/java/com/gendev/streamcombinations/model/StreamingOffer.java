package com.gendev.streamcombinations.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;


@Getter
@Entity
public class StreamingOffer {

    @Id
    private Long id;

    private int streaming_package_id;
    private int live;
    private int highlights;

    public StreamingOffer() {
    }

    public StreamingOffer(Long id, int streaming_package_id, int live, int highlights) {
        this.id = id;
        this.streaming_package_id = streaming_package_id;
        this.live = live;
        this.highlights = highlights;
    }

}
