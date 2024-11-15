package com.gendev.streamcombinations.model;

import lombok.Getter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Getter
@Entity
public class StreamingPackage {

    @Id
    private Long id;

    private String name;
    private int monthly_price_cents;
    private int monthly_price_yearly_subscription_in_cents;

    public StreamingPackage() {
    }

    public StreamingPackage(Long id, String name, int monthly_price_cents, int monthly_price_yearly_subscription_in_cents) {
        this.id = id;
        this.name = name;
        this.monthly_price_cents = monthly_price_cents;
        this.monthly_price_yearly_subscription_in_cents = monthly_price_yearly_subscription_in_cents;
    }
}
