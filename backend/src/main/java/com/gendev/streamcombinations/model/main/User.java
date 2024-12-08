package com.gendev.streamcombinations.model.main;

import java.util.HashSet;
import java.util.Set;

public class User {
    private int balance;
    private Set<Integer> boughtPackageIds;

    public User() {
        this.balance = 100; // Start with 100 CheckPoints
        this.boughtPackageIds = new HashSet<>();
    }

    public int getBalance() {
        return balance;
    }

    public void reduceBalance(int amount) {
        this.balance -= amount;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public Set<Integer> getBoughtPackageIds() {
        return boughtPackageIds;
    }

    public void buyPackage(int packageId) {
        boughtPackageIds.add(packageId);
    }
}
