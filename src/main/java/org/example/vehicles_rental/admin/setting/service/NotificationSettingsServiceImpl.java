package org.example.vehicles_rental.admin.setting.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.NotificationSettingsRequest;
import org.example.vehicles_rental.admin.setting.dto.response.NotificationSettingsResponse;
import org.example.vehicles_rental.admin.setting.entity.NotificationSettings;
import org.example.vehicles_rental.admin.setting.repository.NotificationSettingsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSettingsServiceImpl
        implements NotificationSettingsService {

    private final NotificationSettingsRepository repository;

    @Override
    public NotificationSettingsResponse getSettings() {

        NotificationSettings settings = repository.findById(1L)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Notification settings not found"
                        )
                );

        return mapToResponse(settings);
    }
    @Override
    public NotificationSettingsResponse updateSettings(
            NotificationSettingsRequest request) {

        NotificationSettings settings = repository.findById(1L)
                .orElseGet(NotificationSettings::new);

        settings.setNewBooking(request.isNewBooking());
        settings.setPaymentReceived(request.isPaymentReceived());
        settings.setBookingCancellation(
                request.isBookingCancellation()
        );
        settings.setNewUserRegistration(
                request.isNewUserRegistration()
        );
        settings.setLowAvailabilityAlert(
                request.isLowAvailabilityAlert()
        );
        settings.setDailySummaryReport(
                request.isDailySummaryReport()
        );

        NotificationSettings saved = repository.save(settings);

        return mapToResponse(saved);
    }
    private NotificationSettingsResponse mapToResponse(
            NotificationSettings settings) {

        return new NotificationSettingsResponse(
                settings.getId(),
                settings.isNewBooking(),
                settings.isPaymentReceived(),
                settings.isBookingCancellation(),
                settings.isNewUserRegistration(),
                settings.isLowAvailabilityAlert(),
                settings.isDailySummaryReport()
        );
    }
}