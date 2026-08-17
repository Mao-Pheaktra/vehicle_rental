package org.example.vehicles_rental.admin.setting.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.GeneralSettingsRequest;
import org.example.vehicles_rental.admin.setting.dto.response.GeneralSettingsResponse;
import org.example.vehicles_rental.admin.setting.entity.GeneralSettings;
import org.example.vehicles_rental.admin.setting.repository.GeneralSettingsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneralSettingsServiceImpl implements GeneralSettingsService{
    private final GeneralSettingsRepository repository;

    @Override
    public GeneralSettingsResponse getSettings() {

        GeneralSettings settings = repository.findById(1L)
                .orElseThrow(() ->
                        new RuntimeException(
                                "General settings not found"
                        )
                );

        return mapToResponse(settings);
    }
    @Override
    public GeneralSettingsResponse updateSettings(
            GeneralSettingsRequest request) {

        GeneralSettings settings = repository.findById(1L)
                .orElseGet(GeneralSettings::new);

        settings.setBusinessName(request.getBusinessName());
        settings.setContactEmail(request.getContactEmail());
        settings.setPhoneNumber(request.getPhoneNumber());
        settings.setAddress(request.getAddress());
        settings.setCurrency(request.getCurrency());
        settings.setTimezone(request.getTimezone());

        GeneralSettings saved = repository.save(settings);

        return mapToResponse(saved);
    }
    private GeneralSettingsResponse mapToResponse(
            GeneralSettings settings) {

        return new GeneralSettingsResponse(
                settings.getId(),
                settings.getBusinessName(),
                settings.getContactEmail(),
                settings.getPhoneNumber(),
                settings.getAddress(),
                settings.getCurrency(),
                settings.getTimezone()
        );
    }
}
