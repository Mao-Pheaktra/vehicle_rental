package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
    Optional<Vehicle> findByNameIgnoreCase(String name);
}
