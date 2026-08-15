package org.example.vehicles_rental.admin.report.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.report.dto.ReportAnalyticsResponse;
import org.example.vehicles_rental.admin.report.service.ReportAnalyticsService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin/report")
@RestController
@RequiredArgsConstructor
public class ReportAnalyticController {
    private final ReportAnalyticsService reportAnalyticsService;
    @GetMapping
    public ApiResponse<ReportAnalyticsResponse> getReportAnalytics(){
        return new ApiResponse<>("Get report successfully", 200, reportAnalyticsService.getReportAnalytics());
    }
}
