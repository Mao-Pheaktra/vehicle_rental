package org.example.vehicles_rental.admin.setting.dto.request;
import lombok.Data;

@Data
public class NotificationSettingsRequest {

    private boolean newBooking;

    private boolean paymentReceived;

    private boolean bookingCancellation;

    private boolean newUserRegistration;

    private boolean lowAvailabilityAlert;

    private boolean dailySummaryReport;
}