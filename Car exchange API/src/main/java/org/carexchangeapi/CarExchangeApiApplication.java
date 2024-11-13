package org.carexchangeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "org.carexchangelibrary")
public class CarExchangeApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarExchangeApiApplication.class, args);
    }
}
