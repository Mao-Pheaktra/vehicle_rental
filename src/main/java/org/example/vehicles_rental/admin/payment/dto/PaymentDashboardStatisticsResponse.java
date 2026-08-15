package org.example.vehicles_rental.admin.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDashboardStatisticsResponse {

    private BigDecimal confirmedRevenue;
    private long pending;
    private long totalTransactions;
    private long refunded;
}