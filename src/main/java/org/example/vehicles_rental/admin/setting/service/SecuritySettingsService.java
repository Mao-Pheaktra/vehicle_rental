package org.example.vehicles_rental.admin.setting.service;


import org.example.vehicles_rental.admin.setting.dto.request.SecuritySettingsRequest;
import org.example.vehicles_rental.admin.setting.dto.response.SecuritySettingsResponse;

public interface SecuritySettingsService {

    SecuritySettingsResponse getSettings();

    SecuritySettingsResponse updateSettings(
            SecuritySettingsRequest request
    );
}