package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.Booking;
import org.example.vehicles_rental.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    long countByStatus(BookingStatus status);

    @Query("""
            SELECT COALESCE(SUM(b.totalPrice), 0)
            FROM Booking b
            WHERE b.status = :status
            """)
    BigDecimal sumRevenueByStatus(
            @Param("status") BookingStatus status
    );

    @Query("""
            SELECT COALESCE(SUM(b.totalDays), 0)
            FROM Booking b
            WHERE b.status = :status
            """)
    long sumTotalDaysByStatus(
            @Param("status") BookingStatus status
    );
    //For report and analyst
    @Query("""
    SELECT COALESCE(SUM(b.totalPrice), 0)
    FROM Booking b
    WHERE b.createdAt >= :startDate
    AND b.createdAt < :endDate
    AND b.status = :status
    """)
    BigDecimal sumRevenueBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") BookingStatus status
    );
    @Query("""
    SELECT COUNT(b)
    FROM Booking b
    WHERE b.createdAt >= :startDate
    AND b.createdAt < :endDate
    """)
    long countBookingsBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
//    For monthly bookings:
            @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.createdAt >= :startDate
            AND b.createdAt < :endDate
            """)
        long countBookingsForMonth(
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate
        );
    //    monthly revenue:
        @Query("""
            SELECT COALESCE(SUM(b.totalPrice), 0)
            FROM Booking b
            WHERE b.createdAt >= :startDate
            AND b.createdAt < :endDate
            AND b.status = :status
            """)
        BigDecimal sumRevenueForMonth(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") BookingStatus status
    );
}
