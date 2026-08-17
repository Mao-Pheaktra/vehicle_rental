package org.example.vehicles_rental.admin.setting.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationSettingsResponse {

    private Long id;

    private boolean newBooking;

    private boolean paymentReceived;

    private boolean bookingCancellation;

    private boolean newUserRegistration;

    private boolean lowAvailabilityAlert;

    private boolean dailySummaryReport;
}