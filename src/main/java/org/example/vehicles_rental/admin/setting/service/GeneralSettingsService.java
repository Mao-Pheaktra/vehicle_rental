package org.example.vehicles_rental.admin.setting.service;

import org.example.vehicles_rental.admin.setting.dto.request.GeneralSettingsRequest;
import org.example.vehicles_rental.admin.setting.dto.response.GeneralSettingsResponse;

public interface GeneralSettingsService {
    GeneralSettingsResponse getSettings();
    GeneralSettingsResponse updateSettings(GeneralSettingsRequest request);
}
