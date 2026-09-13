package org.example.vehicles_rental.admin.setting.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.CustomizerRequest;
import org.example.vehicles_rental.admin.setting.dto.response.CustomizerResponse;
import org.example.vehicles_rental.admin.setting.service.CustomizerService;
import org.example.vehicles_rental.dto.request.BrandRequest;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.example.vehicles_rental.dto.response.BrandResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/setting/customizer")
public class CustomizerController {

    private final CustomizerService customizerService;

    @GetMapping
    public ApiResponse<CustomizerResponse> getSettings() {

        return new ApiResponse<>(
                "Get successfully",
                200,
                customizerService.getSettings()
        );
    }

    @PutMapping(consumes = {"multipart/form-data"})
    public ApiResponse<CustomizerResponse> updateSettings(
            @ModelAttribute CustomizerRequest customizerRequest,

            @RequestParam(value = "logoFile", required = false)
            MultipartFile logoFile,

            @RequestParam(value = "heroImageFile", required = false)
            MultipartFile heroImageFile
    ) {

        return new ApiResponse<>(
                "Update successfully",
                200,
                customizerService.updateSettings(customizerRequest, logoFile,heroImageFile)
        );
    }

}