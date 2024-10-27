package com.esm.faceitstats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
}
)
@EnableAsync
public class FaceitStatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaceitStatsApplication.class, args);
    }

}
