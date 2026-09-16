package org.example.vehicles_rental.admin.booking.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.booking.dto.BookingDashboardResponse;
import org.example.vehicles_rental.admin.booking.repository.BookingDashboardRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingDashboardServiceImple implements BookingDashboardService {

    private final BookingDashboardRepository bookingRepository;

    @Override
    public BookingDashboardResponse getBookings() {

        // =========================
        // TOTAL BOOKINGS
        // =========================

        long totalBookings = bookingRepository.count();


        // =========================
        // BOOKINGS BY STATUS
        // =========================

        long pendingBookings =
                bookingRepository.countByStatus(
                        org.example.vehicles_rental.enums.BookingStatus.PENDING
                );

        long confirmedBookings =
                bookingRepository.countByStatus(
                        org.example.vehicles_rental.enums.BookingStatus.CONFIRMED
                );

        long completedBookings =
                bookingRepository.countByStatus(
                        org.example.vehicles_rental.enums.BookingStatus.COMPLETED
                );

        long cancelledBookings =
                bookingRepository.countByStatus(
                        org.example.vehicles_rental.enums.BookingStatus.CANCELLED
                );


        // =========================
        // CURRENT MONTH
        // =========================

        LocalDateTime currentMonthStart =
                LocalDate.now()
                        .withDayOfMonth(1)
                        .atStartOfDay();

        LocalDateTime now =
                LocalDateTime.now();


        // =========================
        // PREVIOUS MONTH
        // =========================

        LocalDateTime previousMonthStart =
                currentMonthStart.minusMonths(1);


        // =========================
        // BOOKING COUNTS
        // =========================

        long currentMonthBookings =
                bookingRepository.countBookingsBetween(
                        currentMonthStart,
                        now
                );

        long previousMonthBookings =
                bookingRepository.countBookingsBetween(
                        previousMonthStart,
                        currentMonthStart
                );


        // =========================
        // GROWTH %
        // =========================

        BigDecimal bookingGrowthPercentage =
                BigDecimal.ZERO;

        if (previousMonthBookings > 0) {

            bookingGrowthPercentage =
                    BigDecimal.valueOf(
                                    currentMonthBookings - previousMonthBookings
                            )
                            .multiply(BigDecimal.valueOf(100))
                            .divide(
                                    BigDecimal.valueOf(previousMonthBookings),
                                    1,
                                    RoundingMode.HALF_UP
                            );
        }


        // =========================
        // CANCELLED RATE %
        // =========================

        BigDecimal cancelledRate =
                BigDecimal.ZERO;

        if (totalBookings > 0) {

            cancelledRate =
                    BigDecimal.valueOf(cancelledBookings)
                            .multiply(BigDecimal.valueOf(100))
                            .divide(
                                    BigDecimal.valueOf(totalBookings),
                                    1,
                                    RoundingMode.HALF_UP
                            );
        }


        // =========================
        // RETURN RESPONSE
        // =========================

        return BookingDashboardResponse.builder()
                .totalBookings(totalBookings)
                .pendingBookings(pendingBookings)
                .confirmedBookings(confirmedBookings)
                .completedBookings(completedBookings)
                .cancelledBookings(cancelledBookings)
                .bookingGrowthPercentage(bookingGrowthPercentage)
                .cancelledRate(cancelledRate)
                .bookings(bookingRepository.findAllBookings())
                .build();
    }
}