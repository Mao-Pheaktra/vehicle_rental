package org.example.vehicles_rental.admin.user.controller;


import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.user.dto.UserStatisticResponse;
import org.example.vehicles_rental.admin.user.service.UserDashboardService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@RestController
public class UserDashboardController {
    private final UserDashboardService userDashboardService;
    @GetMapping
    public ApiResponse< UserStatisticResponse> getUserStatistics(){
        return new ApiResponse<>("Get api user dashboard successfully", 200, userDashboardService.getUserStatistics());
    }
}
