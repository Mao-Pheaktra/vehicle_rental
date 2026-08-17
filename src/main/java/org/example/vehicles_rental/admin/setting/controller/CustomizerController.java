package org.example.vehicles_rental.admin.setting.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.CustomizerRequest;
import org.example.vehicles_rental.admin.setting.dto.response.CustomizerResponse;
import org.example.vehicles_rental.admin.setting.service.CustomizerService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/setting/customizer")
public class CustomizerController {

    private final CustomizerService service;

    @GetMapping
    public ApiResponse<CustomizerResponse> getSettings() {

        return new ApiResponse<>("Get successfully", 200, service.getSettings());
    }

    @PutMapping
    public ApiResponse<CustomizerResponse> updateSettings(
            @RequestBody CustomizerRequest request) {

        return new ApiResponse<>("Update successfully", 200, service.updateSettings(request));
    }
}