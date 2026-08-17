package org.example.vehicles_rental.admin.setting.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.SecuritySettingsRequest;
import org.example.vehicles_rental.admin.setting.dto.response.SecuritySettingsResponse;
import org.example.vehicles_rental.admin.setting.entity.SecuritySettings;
import org.example.vehicles_rental.admin.setting.repository.SecuritySettingsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecuritySettingsServiceImpl implements SecuritySettingsService {
    private final SecuritySettingsRepository repository;

    @Override
    public SecuritySettingsResponse getSettings() {

        SecuritySettings settings = repository.findById(1L)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Security settings not found"
                        )
                );

        return mapToResponse(settings);
    }

    @Override
    public SecuritySettingsResponse updateSettings(
            SecuritySettingsRequest request) {

        SecuritySettings settings = repository.findById(1L)
                .orElseGet(SecuritySettings::new);

        settings.setTwoFactorAuthentication(
                request.isTwoFactorAuthentication()
        );

        settings.setSessionTimeoutMinutes(
                request.getSessionTimeoutMinutes()
        );

        SecuritySettings saved = repository.save(settings);

        return mapToResponse(saved);
    }
    private SecuritySettingsResponse mapToResponse(
            SecuritySettings settings) {

        return new SecuritySettingsResponse(
                settings.getId(),
                settings.isTwoFactorAuthentication(),
                settings.getSessionTimeoutMinutes()
        );
    }
}
