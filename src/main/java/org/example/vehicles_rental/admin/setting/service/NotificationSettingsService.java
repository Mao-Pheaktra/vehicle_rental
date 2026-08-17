package org.example.vehicles_rental.admin.setting.service;

import org.example.vehicles_rental.admin.setting.dto.request.NotificationSettingsRequest;
import org.example.vehicles_rental.admin.setting.dto.response.NotificationSettingsResponse;

public interface NotificationSettingsService {

    NotificationSettingsResponse getSettings();

    NotificationSettingsResponse updateSettings(
            NotificationSettingsRequest request
    );
}