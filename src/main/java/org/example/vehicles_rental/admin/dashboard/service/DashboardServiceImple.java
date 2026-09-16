package org.example.vehicles_rental.admin.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.dashboard.dto.*;
import org.example.vehicles_rental.admin.dashboard.repository.DashboardRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImple implements DashboardService {

    private final DashboardRepository dashboardRepository;

    @Override
    public DashboardResponse getDashboard() {

        // ==========================================
        // 1. Total users
        // ==========================================

        Long totalUser =
                dashboardRepository.countTotalUsers();


        // ==========================================
        // 2. User growth
        // User.createdAt = LocalDateTime
        // ==========================================

        LocalDateTime startCurrentMonthDateTime =
                LocalDate.now()
                        .withDayOfMonth(1)
                        .atStartOfDay();

        LocalDateTime startPreviousMonthDateTime =
                startCurrentMonthDateTime.minusMonths(1);

        LocalDateTime nowDateTime =
                LocalDateTime.now();

        Long currentMonthUsers =
                dashboardRepository.countUsersBetween(
                        startCurrentMonthDateTime,
                        nowDateTime
                );

        Long previousMonthUsers =
                dashboardRepository.countUsersBetween(
                        startPreviousMonthDateTime,
                        startCurrentMonthDateTime
                );

        BigDecimal userGrowthPercentage =
                BigDecimal.ZERO;

        if (previousMonthUsers > 0) {
            userGrowthPercentage =
                    BigDecimal.valueOf(
                                    (currentMonthUsers - previousMonthUsers)
                                            * 100.0
                                            / previousMonthUsers
                            )
                            .setScale(1, RoundingMode.HALF_UP);
        }


        // ==========================================
        // 3. Vehicle statistics
        // ==========================================

        Long totalVehicle =
                dashboardRepository.countTotalVehicles();

        Long availableVehicle =
                dashboardRepository.countAvailableVehicles();


        // ==========================================
        // 4. Booking statistics
        // ==========================================

        Long activeBooking =
                dashboardRepository.countActiveBookings();

        Long pendingBooking =
                dashboardRepository.countPendingBookings();


        // ==========================================
        // 5. Monthly revenue
        // Booking.returnDate = LocalDate
        // ==========================================

        BigDecimal monthlyRevenue =
                dashboardRepository.getMonthlyRevenue();


        // ==========================================
        // 6. Revenue growth
        // ==========================================

        LocalDate startCurrentMonth =
                LocalDate.now()
                        .withDayOfMonth(1);

        LocalDate startPreviousMonth =
                startCurrentMonth.minusMonths(1);

        LocalDate endCurrentMonth =
                LocalDate.now()
                        .plusDays(1);


        BigDecimal currentMonthRevenue =
                dashboardRepository.getRevenueBetween(
                        startCurrentMonth,
                        endCurrentMonth
                );

        BigDecimal previousMonthRevenue =
                dashboardRepository.getRevenueBetween(
                        startPreviousMonth,
                        startCurrentMonth
                );


        BigDecimal revenueGrowthPercentage =
                BigDecimal.ZERO;

        if (previousMonthRevenue.compareTo(BigDecimal.ZERO) > 0) {

            revenueGrowthPercentage =
                    currentMonthRevenue
                            .subtract(previousMonthRevenue)
                            .multiply(BigDecimal.valueOf(100))
                            .divide(
                                    previousMonthRevenue,
                                    1,
                                    RoundingMode.HALF_UP
                            );
        }


        // ==========================================
        // 7. Revenue chart
        // ==========================================

        List<RevenueResponse> revenueResponses =
                dashboardRepository.getRevenueStatistics();


        // ==========================================
        // 8. Booking status statistics
        // ==========================================

        List<BookingStatisticResponse> bookingStatistics =
                dashboardRepository.getBookingStatistics();


        // ==========================================
        // 9. Vehicle category statistics
        // ==========================================

        List<CategoryStatisticResponse> categoryStatistics =
                dashboardRepository.getCategoryStatistics();


        // ==========================================
        // 10. Recent bookings
        // ==========================================

        List<RecentBookingResponse> recentBookings =
                dashboardRepository.getRecentBookings();


        // ==========================================
        // 11. Return dashboard response
        // ==========================================

        return DashboardResponse.builder()
                .totalUser(totalUser)
                .userGrowthPercentage(userGrowthPercentage)

                .pendingBooking(pendingBooking)

                .totalVehicle(totalVehicle)
                .availableVehicle(availableVehicle)
                .activeBooking(activeBooking)

                .monthlyRevenue(monthlyRevenue)
                .revenueGrowthPercentage(revenueGrowthPercentage)

                .revenueResponses(revenueResponses)
                .bookingStatisticResponses(bookingStatistics)
                .categoryStatisticResponses(categoryStatistics)
                .recentBookingResponses(recentBookings)

                .build();
    }
}