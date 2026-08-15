package org.example.vehicles_rental.admin.category.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.category.dto.CategoryDashboardStatisticsResponse;
import org.example.vehicles_rental.admin.category.service.CategoryDashboardService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin/category")
@RestController
@RequiredArgsConstructor
public class CategoryDashboardController {
    private final CategoryDashboardService categoryDashboardService;
    @GetMapping
    public ApiResponse<CategoryDashboardStatisticsResponse> getCategoryStatistics(){
        return new ApiResponse<>("Get api category dashboard successfully", 200, categoryDashboardService.getCategoryStatistics());
    }
}
