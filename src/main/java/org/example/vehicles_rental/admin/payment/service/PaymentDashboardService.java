package org.example.vehicles_rental.admin.payment.service;


import org.example.vehicles_rental.admin.payment.dto.PaymentDashboardStatisticsResponse;

public interface PaymentDashboardService {
    PaymentDashboardStatisticsResponse getPaymentStatistics();
}
