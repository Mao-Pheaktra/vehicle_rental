package org.example.vehicles_rental.admin.setting.service;

import org.example.vehicles_rental.admin.setting.dto.request.CustomizerRequest;
import org.example.vehicles_rental.admin.setting.dto.response.CustomizerResponse;

public interface CustomizerService {
    CustomizerResponse getSettings();
    CustomizerResponse updateSettings(CustomizerRequest request);
}
