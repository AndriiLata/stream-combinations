package com.gendev.streamcombinations.controller;

import com.gendev.streamcombinations.model.main.User;
import com.gendev.streamcombinations.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/status")
    public User getUserStatus() {
        return userService.getUser();
    }

    @PostMapping("/buy")
    public User buyPackage(@RequestParam int packageId, @RequestParam int costInCents) {
        userService.buyPackage(packageId, costInCents);
        return userService.getUser();
    }

    @PostMapping("/top-up")
    public User topUp(@RequestParam int amount) {
        userService.topUp(amount);
        return userService.getUser();
    }

}
