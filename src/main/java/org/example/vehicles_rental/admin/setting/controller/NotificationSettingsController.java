package org.example.vehicles_rental.admin.setting.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.NotificationSettingsRequest;
import org.example.vehicles_rental.admin.setting.dto.response.NotificationSettingsResponse;
import org.example.vehicles_rental.admin.setting.service.NotificationSettingsService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/setting/notification")
@RequiredArgsConstructor
public class NotificationSettingsController {
    private final NotificationSettingsService notificationSettingsService;
    @GetMapping
    public ApiResponse<NotificationSettingsResponse> getSettings(){
        return new ApiResponse<>("Get successfully", 200, notificationSettingsService.getSettings());
    }
    @PutMapping
    public ApiResponse<NotificationSettingsResponse> updateSettings(@RequestBody NotificationSettingsRequest request){
        return new ApiResponse<>("Get successfully", 200, notificationSettingsService.updateSettings(request));
    }

}
