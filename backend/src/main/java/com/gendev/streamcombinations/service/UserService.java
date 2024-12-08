package com.gendev.streamcombinations.service;

import com.gendev.streamcombinations.model.main.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private User user;

    public UserService() {
        this.user = new User();
    }

    public User getUser() {
        return user;
    }

    public void buyPackage(int packageId, int costInCents) {
        int costInCheckPoints = costInCents / 100;
        if (user.getBalance() < costInCheckPoints) {
            // Not enough balance
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Not enough CheckPoints."
            );
        }
        user.reduceBalance(costInCheckPoints);
        user.buyPackage(packageId);
    }

    public void topUp(int amount) {
        user.addBalance(amount);
    }


}
