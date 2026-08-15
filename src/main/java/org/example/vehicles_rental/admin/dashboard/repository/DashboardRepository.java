package org.example.vehicles_rental.admin.dashboard.repository;

import org.example.vehicles_rental.admin.dashboard.dto.BookingStatisticResponse;
import org.example.vehicles_rental.admin.dashboard.dto.CategoryStatisticResponse;
import org.example.vehicles_rental.admin.dashboard.dto.RecentBookingResponse;
import org.example.vehicles_rental.admin.dashboard.dto.RevenueResponse;
import org.example.vehicles_rental.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardRepository extends JpaRepository<Vehicle, Long> {

    // 1. Total users
    @Query("""
            SELECT COUNT(u)
            FROM User u
            """)
    Long countTotalUsers();


    // 2. Total vehicles
    @Query("""
            SELECT COUNT(v)
            FROM Vehicle v
            """)
    Long countTotalVehicles();


    // 3. Available vehicles
    @Query("""
            SELECT COUNT(v)
            FROM Vehicle v
            WHERE v.status = 'AVAILABLE'
            """)
    Long countAvailableVehicles();


    // 4. Active bookings
    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.status = 'ACTIVE'
            """)
    Long countActiveBookings();


    // 5. Current month's revenue
    @Query("""
            SELECT COALESCE(SUM(b.totalPrice), 0)
            FROM Booking b
            WHERE b.status = 'COMPLETED'
            AND MONTH(b.returnDate) = MONTH(CURRENT_DATE)
            AND YEAR(b.returnDate) = YEAR(CURRENT_DATE)
            """)
    BigDecimal getMonthlyRevenue();


    // 6. Revenue statistics
    @Query("""
        SELECT new org.example.vehicles_rental.admin.dashboard.dto.RevenueResponse(
            CAST(MONTH(b.returnDate) AS string),
            COALESCE(SUM(b.totalPrice), 0)
        )
        FROM Booking b
        WHERE b.status = 'COMPLETED'
        GROUP BY MONTH(b.returnDate)
        ORDER BY MONTH(b.returnDate)
        """)
    List<RevenueResponse> getRevenueStatistics();


    // 7. Booking statistics
    @Query("""
            SELECT new org.example.vehicles_rental.admin.dashboard.dto.BookingStatisticResponse(
                CAST(b.status AS string),
                COUNT(b)
            )
            FROM Booking b
            GROUP BY b.status
            """)
    List<BookingStatisticResponse> getBookingStatistics();


    // 8. Category statistics
    @Query("""
            SELECT new org.example.vehicles_rental.admin.dashboard.dto.CategoryStatisticResponse(
                c.category_name,
                COUNT(v)
            )
            FROM Vehicle v
            JOIN v.category c
            GROUP BY c.category_name
            """)
    List<CategoryStatisticResponse> getCategoryStatistics();


    // 9. Recent bookings
    @Query("""
            SELECT new org.example.vehicles_rental.admin.dashboard.dto.RecentBookingResponse(
                b.id,
                u.name,
                v.model,
                b.pickupDate,
                b.returnDate,
                CAST(b.status AS string),
                b.totalPrice
            )
            FROM Booking b
            JOIN b.user u
            JOIN b.vehicle v
            ORDER BY b.id DESC
            """)
    List<RecentBookingResponse> getRecentBookings();
}