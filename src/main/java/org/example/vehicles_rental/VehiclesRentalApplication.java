package org.example.vehicles_rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VehiclesRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehiclesRentalApplication.class, args);
    }
}