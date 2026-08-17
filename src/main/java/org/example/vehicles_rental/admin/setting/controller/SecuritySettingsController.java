package org.example.vehicles_rental.admin.setting.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.ChangePasswordRequest;
import org.example.vehicles_rental.admin.setting.dto.request.SecuritySettingsRequest;
import org.example.vehicles_rental.admin.setting.dto.response.SecuritySettingsResponse;
import org.example.vehicles_rental.admin.setting.service.SecuritySettingsService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.example.vehicles_rental.service.AuthService;
import org.example.vehicles_rental.service.UserService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/setting/security")
public class SecuritySettingsController {
    private final SecuritySettingsService service;
    private final UserService userService;
    @GetMapping
    public ApiResponse<SecuritySettingsResponse> getSettings(){
        return new ApiResponse<>("Get successfully", 200, service.getSettings());
    }
    @PutMapping
    public ApiResponse<SecuritySettingsResponse> updateSettings(@RequestBody SecuritySettingsRequest request){
        return new ApiResponse<>("Get successfully", 200, service.updateSettings(request));
    }
    @PutMapping("/change_pwd")
    public ApiResponse<?> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest) {

        userService.changePassword(changePasswordRequest);

        return new ApiResponse<>(
                "Password changed successfully",
                200,
                null
        );
    }
}
