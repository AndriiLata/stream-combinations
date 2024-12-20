package com.gendev.streamcombinations.model.main;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class User {
    private int balance;
    private final Set<Integer> boughtPackageIds;

    public User() {
        this.balance = 100; // Start with 100 CheckPoints
        this.boughtPackageIds = new HashSet<>();
    }

    public void reduceBalance(int amount) {
        this.balance -= amount;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public void buyPackage(int packageId) {
        boughtPackageIds.add(packageId);
    }
}
