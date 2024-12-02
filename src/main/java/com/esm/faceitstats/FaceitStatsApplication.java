package com.esm.faceitstats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
public class FaceitStatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaceitStatsApplication.class, args);
    }

}
