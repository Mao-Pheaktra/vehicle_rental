package org.example.vehicles_rental.admin.setting.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.PaymentSettingsRequest;
import org.example.vehicles_rental.admin.setting.dto.response.PaymentSettingsResponse;
import org.example.vehicles_rental.admin.setting.service.PaymentSettingsService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/setting/payment")
public class PaymentSettingsController {
    private final PaymentSettingsService service;
    @GetMapping
    public ApiResponse<PaymentSettingsResponse> getSettings(){
        return new ApiResponse<>("Get sucessfully", 200, service.getSettings());
    }
    @PutMapping
    public ApiResponse<PaymentSettingsResponse> updateSettings(@RequestBody PaymentSettingsRequest request){
        return new ApiResponse<>("Update sucessfully", 200, service.updateSettings(request));
    }
}
