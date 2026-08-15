package org.example.vehicles_rental.admin.payment.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.payment.dto.PaymentDashboardStatisticsResponse;
import org.example.vehicles_rental.enums.PaymentStatus;
import org.example.vehicles_rental.repository.PaymentDashboardRepository;
import org.example.vehicles_rental.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentDashboardServiceImple implements PaymentDashboardService {

    private final PaymentDashboardRepository paymentDashboardRepository;

    @Override
    public PaymentDashboardStatisticsResponse getPaymentStatistics() {

        BigDecimal confirmedRevenue =
                paymentDashboardRepository.sumAmountByStatus(
                        PaymentStatus.PAID
                );

        long pending =
                paymentDashboardRepository.countByStatus(
                        PaymentStatus.PENDING
                );

        long totalTransactions =
                paymentDashboardRepository.count();

        long refunded =
                paymentDashboardRepository.countByStatus(
                        PaymentStatus.REFUNDED
                );

        return new PaymentDashboardStatisticsResponse(
                confirmedRevenue,
                pending,
                totalTransactions,
                refunded
        );
    }
}