package org.example.vehicles_rental.admin.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportAnalyticsResponse {

    private BigDecimal totalRevenueYtd;

    private long totalBookingsYtd;

    private BigDecimal averageBookingValue;

    private double customerRetention;

    private long returningCustomers;

    private double averageRatingGiven;

    private long supportTickets;

    private List<MonthlyAnalyticsResponse> monthlyData;
}