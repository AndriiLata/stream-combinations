package com.gendev.streamcombinations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StreamCombinationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamCombinationsApplication.class, args);

    }

}



