package org.example.vehicles_rental.admin.vehicle.repository;

import org.example.vehicles_rental.entity.Vehicle;
import org.example.vehicles_rental.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleDashboardRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.status = :status")
    long countByStatus(@Param("status") Status status);
}
