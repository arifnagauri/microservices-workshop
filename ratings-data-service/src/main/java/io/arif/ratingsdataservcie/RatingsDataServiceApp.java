package io.arif.ratingsdataservcie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RatingsDataServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(RatingsDataServiceApp.class, args);
    }
}
