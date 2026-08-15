package org.example.vehicles_rental.admin.rentalHistory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalHistoryDashboardResponse {

    private long total;
    private long completed;
    private BigDecimal revenue;
    private double averageRating;
    private long totalDaysRented;
}