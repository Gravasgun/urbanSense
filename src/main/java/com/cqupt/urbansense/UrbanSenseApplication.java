package com.cqupt.urbansense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class UrbanSenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrbanSenseApplication.class, args);
    }

}
