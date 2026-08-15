package org.example.vehicles_rental.admin.dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.dashboard.dto.DashboardResponse;
import org.example.vehicles_rental.admin.dashboard.service.DashboardService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin/dashboard")
@RestController
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public ApiResponse<DashboardResponse> getDashboard(){
        return new ApiResponse<>("Get data successfully", 200, dashboardService.getDashboard());
    }
}
