package com.gendev.streamcombinations;

import com.gendev.streamcombinations.service.HandleGames;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StreamCombinationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamCombinationsApplication.class, args);

        HandleGames handleGames = new HandleGames();
        handleGames.printGames();
    }

}
