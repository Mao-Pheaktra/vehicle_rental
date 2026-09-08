package org.example.vehicles_rental.admin.booking.repository;

import org.example.vehicles_rental.entity.Booking;
import org.example.vehicles_rental.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingDashboardRepository extends JpaRepository<Booking, Long> {

    long countByStatus(BookingStatus status);

    @Query("""
        SELECT b
        FROM Booking b
        JOIN FETCH b.user
        JOIN FETCH b.vehicle
        ORDER BY b.id DESC
        """)
    List<Booking> findAllBookings();

    @Query("""
    SELECT COUNT(b)
    FROM Booking b
    WHERE b.createdAt >= :start
    AND b.createdAt < :end
    """)
    long countBookingsBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
