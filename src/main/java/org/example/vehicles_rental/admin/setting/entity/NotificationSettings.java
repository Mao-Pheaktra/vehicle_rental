package org.example.vehicles_rental.admin.setting.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "notification_settings")
@Data
public class NotificationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean newBooking;

    private boolean paymentReceived;

    private boolean bookingCancellation;

    private boolean newUserRegistration;

    private boolean lowAvailabilityAlert;

    private boolean dailySummaryReport;
}