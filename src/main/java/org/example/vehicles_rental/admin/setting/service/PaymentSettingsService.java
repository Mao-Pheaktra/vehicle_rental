package org.example.vehicles_rental.admin.setting.service;

import org.example.vehicles_rental.admin.setting.dto.request.PaymentSettingsRequest;
import org.example.vehicles_rental.admin.setting.dto.response.PaymentSettingsResponse;

public interface PaymentSettingsService {

    PaymentSettingsResponse getSettings();

    PaymentSettingsResponse updateSettings(
            PaymentSettingsRequest request
    );
}