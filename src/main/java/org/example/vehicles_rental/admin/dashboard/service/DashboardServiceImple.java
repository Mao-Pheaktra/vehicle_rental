package org.example.vehicles_rental.admin.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.dashboard.dto.*;
import org.example.vehicles_rental.admin.dashboard.repository.DashboardRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImple implements DashboardService {

    private final DashboardRepository dashboardRepository;

    @Override
    public DashboardResponse getDashboard() {

        Long totalUser = dashboardRepository.countTotalUsers();

        Long totalVehicle = dashboardRepository.countTotalVehicles();

        Long availableVehicle =
                dashboardRepository.countAvailableVehicles();

        Long activeBooking =
                dashboardRepository.countActiveBookings();

        BigDecimal monthlyRevenue =
                dashboardRepository.getMonthlyRevenue();

        List<RevenueResponse> revenueResponses =
                dashboardRepository.getRevenueStatistics();

        List<BookingStatisticResponse> bookingStatistics =
                dashboardRepository.getBookingStatistics();

        List<CategoryStatisticResponse> categoryStatistics =
                dashboardRepository.getCategoryStatistics();

        List<RecentBookingResponse> recentBookings =
                dashboardRepository.getRecentBookings();
        return DashboardResponse.builder()
                .totalUser(totalUser)
                .totalVehicle(totalVehicle)
                .availableVehicle(availableVehicle)
                .activeBooking(activeBooking)
                .monthlyRevenue(monthlyRevenue)
                .revenueResponses(revenueResponses)
                .bookingStatisticResponses(bookingStatistics)
                .categoryStatisticResponses(categoryStatistics)
                .recentBookingResponses(recentBookings)
                .build();
    }
}