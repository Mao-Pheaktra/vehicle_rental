package org.example.vehicles_rental.admin.report.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.report.dto.MonthlyAnalyticsResponse;
import org.example.vehicles_rental.admin.report.dto.ReportAnalyticsResponse;
import org.example.vehicles_rental.admin.report.dto.WeeklyAnalyticsResponse;
import org.example.vehicles_rental.admin.report.dto.YearlyAnalyticsResponse;
import org.example.vehicles_rental.enums.BookingStatus;
import org.example.vehicles_rental.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
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

        // ==========================================
        // TOTAL REVENUE YTD
        // ==========================================

        BigDecimal totalRevenueYtd =
                bookingRepository.sumRevenueBetween(
                        startOfYear,
                        startOfNextYear,
                        BookingStatus.COMPLETED
                );

        if (totalRevenueYtd == null) {
            totalRevenueYtd = BigDecimal.ZERO;
        }

        // ==========================================
        // TOTAL BOOKINGS YTD
        // ==========================================

        long totalBookingsYtd =
                bookingRepository.countBookingsBetween(
                        startOfYear,
                        startOfNextYear
                );

        // ==========================================
        // AVERAGE BOOKING VALUE
        // ==========================================

        BigDecimal averageBookingValue =
                totalBookingsYtd > 0
                        ? totalRevenueYtd.divide(
                        BigDecimal.valueOf(totalBookingsYtd),
                        2,
                        RoundingMode.HALF_UP
                )
                        : BigDecimal.ZERO;

        // ==========================================
        // TEMPORARY VALUES
        // ==========================================

        double customerRetention = 0.0;
        long returningCustomers = 0;
        double averageRatingGiven = 0.0;
        long supportTickets = 0;

        // ==========================================
        // WEEKLY DATA
        // Current month divided into weeks
        // ==========================================

        List<WeeklyAnalyticsResponse> weeklyData =
                new ArrayList<>();

        LocalDate today = LocalDate.now();

        LocalDate startOfCurrentMonth =
                today.withDayOfMonth(1);

        LocalDate endOfCurrentMonth =
                today.with(
                        TemporalAdjusters.lastDayOfMonth()
                );

        int weekNumber = 1;

        LocalDate weekStart = startOfCurrentMonth;

        while (!weekStart.isAfter(endOfCurrentMonth)) {

            LocalDate weekEnd =
                    weekStart.plusDays(6);

            if (weekEnd.isAfter(endOfCurrentMonth)) {
                weekEnd = endOfCurrentMonth;
            }

            LocalDateTime startDateTime =
                    weekStart.atStartOfDay();

            LocalDateTime endDateTime =
                    weekEnd.plusDays(1).atStartOfDay();

            BigDecimal revenue =
                    bookingRepository.sumRevenueBetween(
                            startDateTime,
                            endDateTime,
                            BookingStatus.COMPLETED
                    );

            if (revenue == null) {
                revenue = BigDecimal.ZERO;
            }

            long bookings =
                    bookingRepository.countBookingsBetween(
                            startDateTime,
                            endDateTime
                    );

            weeklyData.add(
                    WeeklyAnalyticsResponse.builder()
                            .label("Week " + weekNumber)
                            .revenue(revenue)
                            .bookings(bookings)
                            .build()
            );

            weekStart = weekEnd.plusDays(1);
            weekNumber++;
        }

        // ==========================================
        // MONTHLY DATA
        // ==========================================

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
                    bookingRepository.sumRevenueBetween(
                            startOfMonth,
                            startOfNextMonth,
                            BookingStatus.COMPLETED
                    );

            if (revenue == null) {
                revenue = BigDecimal.ZERO;
            }

            long bookings =
                    bookingRepository.countBookingsBetween(
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

        // ==========================================
        // YEARLY DATA
        // Last 5 years
        // ==========================================

        List<YearlyAnalyticsResponse> yearlyData =
                new ArrayList<>();

        int firstYear = year - 4;

        for (int currentYear = firstYear;
             currentYear <= year;
             currentYear++) {

            LocalDateTime startOfYearDateTime =
                    LocalDate.of(currentYear, 1, 1)
                            .atStartOfDay();

            LocalDateTime startOfNextYearDateTime =
                    LocalDate.of(currentYear + 1, 1, 1)
                            .atStartOfDay();

            BigDecimal revenue =
                    bookingRepository.sumRevenueBetween(
                            startOfYearDateTime,
                            startOfNextYearDateTime,
                            BookingStatus.COMPLETED
                    );

            if (revenue == null) {
                revenue = BigDecimal.ZERO;
            }

            long bookings =
                    bookingRepository.countBookingsBetween(
                            startOfYearDateTime,
                            startOfNextYearDateTime
                    );

            yearlyData.add(
                    YearlyAnalyticsResponse.builder()
                            .year(currentYear)
                            .revenue(revenue)
                            .bookings(bookings)
                            .build()
            );
        }

        // ==========================================
        // RETURN RESPONSE
        // ==========================================

        return ReportAnalyticsResponse.builder()
                .totalRevenueYtd(totalRevenueYtd)
                .totalBookingsYtd(totalBookingsYtd)
                .averageBookingValue(averageBookingValue)
                .customerRetention(customerRetention)
                .returningCustomers(returningCustomers)
                .averageRatingGiven(averageRatingGiven)
                .supportTickets(supportTickets)
                .weeklyData(weeklyData)
                .monthlyData(monthlyData)
                .yearlyData(yearlyData)
                .build();
    }
}