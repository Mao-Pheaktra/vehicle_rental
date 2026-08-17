package org.example.vehicles_rental.admin.setting.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.GeneralSettingsRequest;
import org.example.vehicles_rental.admin.setting.dto.response.GeneralSettingsResponse;
import org.example.vehicles_rental.admin.setting.service.GeneralSettingsService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/setting/general")
public class GeneralSettingsController {
    private final GeneralSettingsService generalSettingsService;
    @GetMapping
    public ApiResponse<GeneralSettingsResponse> getSettings(){
        return new ApiResponse<>("Get api setting successfully", 200, generalSettingsService.getSettings());
    }
    @PutMapping
    public ApiResponse<GeneralSettingsResponse> updateSettings(@RequestBody GeneralSettingsRequest request){
        return new ApiResponse<>("Update successfully", 200 , generalSettingsService.updateSettings(request));
    }
}
