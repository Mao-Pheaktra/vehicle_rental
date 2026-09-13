package org.example.vehicles_rental.admin.rentalHistory.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.rentalHistory.dto.RentalHistoryDashboardResponse;
import org.example.vehicles_rental.admin.rentalHistory.repository.RentalHistoryRepository;
import org.example.vehicles_rental.enums.BookingStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RentalHistoryDashboardServiceImple implements RentalHistoryDashboardService {

    private final RentalHistoryRepository rentalHistoryRepository;

    @Override
    public RentalHistoryDashboardResponse getStatistics() {

        long total =
                rentalHistoryRepository.count();

        long completed =
                rentalHistoryRepository.countByStatus(
                        BookingStatus.COMPLETED
                );

        BigDecimal revenue =
                rentalHistoryRepository.sumRevenueByStatus(
                        BookingStatus.COMPLETED
                );

        long totalDaysRented =
                rentalHistoryRepository.sumTotalDaysByStatus(
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