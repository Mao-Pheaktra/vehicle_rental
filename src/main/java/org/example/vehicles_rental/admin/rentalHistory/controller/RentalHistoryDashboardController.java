package org.example.vehicles_rental.admin.rentalHistory.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.rentalHistory.dto.RentalHistoryDashboardResponse;
import org.example.vehicles_rental.admin.rentalHistory.service.RentalHistoryDashboardService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/rental_history")
public class RentalHistoryDashboardController {
    private final RentalHistoryDashboardService rentalHistoryDashboardService;
    @GetMapping
    public ApiResponse<RentalHistoryDashboardResponse> etStatistics(){
        return new ApiResponse<>("Get api rental history successfully", 200, rentalHistoryDashboardService.getStatistics());
    }
}
