package org.example.vehicles_rental.admin.report.service;


import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.report.dto.MonthlyAnalyticsResponse;
import org.example.vehicles_rental.admin.report.dto.ReportAnalyticsResponse;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.enums.BookingStatus;
import org.example.vehicles_rental.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportAnalyticsServiceImple
        implements ReportAnalyticsService {

    private final BookingRepository bookingRepository;

    @Override
    public ReportAnalyticsResponse getReportAnalytics() {

        int year = Year.now().getValue();

        LocalDateTime startOfYear =
                LocalDate.of(year, 1, 1).atStartOfDay();

        LocalDateTime startOfNextYear =
                LocalDate.of(year + 1, 1, 1).atStartOfDay();

        // Total Revenue YTD
        BigDecimal totalRevenueYtd =
                bookingRepository.sumRevenueBetween(
                        startOfYear,
                        startOfNextYear,
                        BookingStatus.COMPLETED
                );

        // Total Bookings YTD
        long totalBookingsYtd =
                bookingRepository.countBookingsBetween(
                        startOfYear,
                        startOfNextYear
                );

        // Average Booking Value
        BigDecimal averageBookingValue =
                totalBookingsYtd > 0
                        ? totalRevenueYtd.divide(
                        BigDecimal.valueOf(totalBookingsYtd),
                        2,
                        java.math.RoundingMode.HALF_UP
                )
                        : BigDecimal.ZERO;

        // Temporary values because we don't have
        // Rating and Support Ticket entities yet
        double customerRetention = 0.0;
        long returningCustomers = 0;
        double averageRatingGiven = 0.0;
        long supportTickets = 0;

        // Monthly graph
        List<MonthlyAnalyticsResponse> monthlyData =
                new ArrayList<>();

        for (int month = 1; month <= 12; month++) {

            LocalDateTime startOfMonth =
                    LocalDate.of(year, month, 1)
                            .atStartOfDay();

            LocalDateTime startOfNextMonth =
                    month == 12
                            ? LocalDate.of(year + 1, 1, 1)
                            .atStartOfDay()
                            : LocalDate.of(year, month + 1, 1)
                            .atStartOfDay();

            BigDecimal revenue =
                    bookingRepository.sumRevenueForMonth(
                            startOfMonth,
                            startOfNextMonth,
                            BookingStatus.COMPLETED
                    );

            long bookings =
                    bookingRepository.countBookingsForMonth(
                            startOfMonth,
                            startOfNextMonth
                    );

            monthlyData.add(
                    MonthlyAnalyticsResponse.builder()
                            .month(
                                    Month.of(month)
                                            .name()
                            )
                            .revenue(revenue)
                            .bookings(bookings)
                            .build()
            );
        }

        return ReportAnalyticsResponse.builder()
                .totalRevenueYtd(totalRevenueYtd)
                .totalBookingsYtd(totalBookingsYtd)
                .averageBookingValue(averageBookingValue)
                .customerRetention(customerRetention)
                .returningCustomers(returningCustomers)
                .averageRatingGiven(averageRatingGiven)
                .supportTickets(supportTickets)
                .monthlyData(monthlyData)
                .build();
    }
}