package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByVehicleId(Long vehicleId);
    List<Booking> findByStatus(String status);
    boolean existsByVehicleIdAndPickupDateLessThanAndReturnDateGreaterThan(
            Long vehicleId,
            LocalDate returnDate,
            LocalDate pickupDate
    );
    boolean existsByVehicleIdAndIdNotAndPickupDateLessThanAndReturnDateGreaterThan(
            Long vehicleId,
            Long bookingId,
            LocalDate returnDate,
            LocalDate pickupDate
    );
}