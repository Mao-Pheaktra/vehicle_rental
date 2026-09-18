package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserIdAndVehicleId(Long userId, Long vehicleId);

    boolean existsByUserIdAndVehicleId(Long userId, Long vehicleId);

    List<Favorite> findByUserId(Long userId);

    void deleteByUserIdAndVehicleId(Long userId, Long vehicleId);
}