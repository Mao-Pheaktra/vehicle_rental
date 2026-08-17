package org.example.vehicles_rental.admin.setting.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.PaymentSettingsRequest;
import org.example.vehicles_rental.admin.setting.dto.response.PaymentSettingsResponse;
import org.example.vehicles_rental.admin.setting.entity.PaymentSettings;
import org.example.vehicles_rental.admin.setting.repository.PaymentSettingsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentSettingsServiceImpl implements PaymentSettingsService{
    private final PaymentSettingsRepository repository;

    @Override
    public PaymentSettingsResponse getSettings() {

        PaymentSettings settings = repository.findById(1L)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment settings not found"
                        )
                );

        return mapToResponse(settings);
    }
    @Override
    public PaymentSettingsResponse updateSettings(
            PaymentSettingsRequest request) {

        PaymentSettings settings = repository.findById(1L)
                .orElseGet(PaymentSettings::new);

        settings.setCashEnabled(
                request.isCashEnabled()
        );

        settings.setAbaKhqrEnabled(
                request.isAbaKhqrEnabled()
        );

        settings.setCardEnabled(
                request.isCardEnabled()
        );

        settings.setBakongEnabled(
                request.isBakongEnabled()
        );
        PaymentSettings saved = repository.save(settings);

        return mapToResponse(saved);
    }
    private PaymentSettingsResponse mapToResponse(
            PaymentSettings settings) {

        return new PaymentSettingsResponse(
                settings.getId(),
                settings.isCashEnabled(),
                settings.isAbaKhqrEnabled(),
                settings.isCardEnabled(),
                settings.isBakongEnabled()

        );
    }
}
