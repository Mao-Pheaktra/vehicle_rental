package org.example.vehicles_rental.admin.vehicle.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.vehicle.dto.VehicleStatisticsResponse;
import org.example.vehicles_rental.admin.vehicle.service.VehicleDashboardService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/vehicle")
public class VehicleDashboardController {

    private final VehicleDashboardService vehicleDashboardService;
    @GetMapping
    public ApiResponse<VehicleStatisticsResponse> getVehicleStatistics(){
        return new ApiResponse<>("Get api vehicle dashboard successfully", 200, vehicleDashboardService.getVehicleStatistics());
    }
}
