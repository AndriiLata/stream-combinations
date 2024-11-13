package com.gendev.streamcombinations.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class Streaming_Package {

    @Id
    private Long id;

    private String name;
    private int monthly_price_cents;
    private int monthly_price_yearly_subscription_in_cents;

    public Streaming_Package() {
    }

    public Streaming_Package(Long id, String name, int monthly_price_cents, int monthly_price_yearly_subscription_in_cents) {
        this.id = id;
        this.name = name;
        this.monthly_price_cents = monthly_price_cents;
        this.monthly_price_yearly_subscription_in_cents = monthly_price_yearly_subscription_in_cents;
    }
}
