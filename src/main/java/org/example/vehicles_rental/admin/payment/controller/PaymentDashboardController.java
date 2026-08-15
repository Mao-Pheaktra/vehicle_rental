package org.example.vehicles_rental.admin.payment.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.payment.dto.PaymentDashboardStatisticsResponse;
import org.example.vehicles_rental.admin.payment.service.PaymentDashboardService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/payment")
@RequiredArgsConstructor
public class PaymentDashboardController {
    private final PaymentDashboardService paymentDashboardService;
    @GetMapping
    public ApiResponse<PaymentDashboardStatisticsResponse> getPaymentStatistics(){
        return new ApiResponse<>("Get api payment dashboard successfully", 200, paymentDashboardService.getPaymentStatistics());
    }
}
