package org.example.vehicles_rental.admin.rentalHistory.repository;


import org.example.vehicles_rental.entity.Booking;
import org.example.vehicles_rental.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface BookingDashboardRepository
        extends JpaRepository<Booking, Long> {

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
}