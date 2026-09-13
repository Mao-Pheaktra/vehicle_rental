package org.example.vehicles_rental.admin.setting.service;

import org.example.vehicles_rental.admin.setting.dto.request.CustomizerRequest;
import org.example.vehicles_rental.admin.setting.dto.response.CustomizerResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CustomizerService {
    CustomizerResponse getSettings();
    CustomizerResponse updateSettings(CustomizerRequest customizerRequest,  MultipartFile logoFile, MultipartFile heroImageFile);
}
