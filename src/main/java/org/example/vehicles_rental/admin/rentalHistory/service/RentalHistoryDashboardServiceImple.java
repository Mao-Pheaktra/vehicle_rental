package org.example.vehicles_rental.admin.rentalHistory.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.rentalHistory.dto.RentalHistoryDashboardResponse;
import org.example.vehicles_rental.enums.BookingStatus;
import org.example.vehicles_rental.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RentalHistoryDashboardServiceImple implements RentalHistoryDashboardService {

    private final BookingRepository bookingRepository;

    @Override
    public RentalHistoryDashboardResponse getStatistics() {

        long total =
                bookingRepository.count();

        long completed =
                bookingRepository.countByStatus(
                        BookingStatus.COMPLETED
                );

        BigDecimal revenue =
                bookingRepository.sumRevenueByStatus(
                        BookingStatus.COMPLETED
                );

        long totalDaysRented =
                bookingRepository.sumTotalDaysByStatus(
                        BookingStatus.COMPLETED
                );

        // No rating field in Booking yet
        double averageRating = 0.0;

        return new RentalHistoryDashboardResponse(
                total,
                completed,
                revenue,
                averageRating,
                totalDaysRented
        );
    }
}