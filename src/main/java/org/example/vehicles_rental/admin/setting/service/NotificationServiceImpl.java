package org.example.vehicles_rental.admin.setting.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.response.NotificationResponse;
import org.example.vehicles_rental.admin.setting.entity.Notification;
import org.example.vehicles_rental.admin.setting.entity.NotificationSettings;
import org.example.vehicles_rental.admin.setting.repository.NotificationRepository;
import org.example.vehicles_rental.admin.setting.repository.NotificationSettingsRepository;
import org.example.vehicles_rental.entity.Booking;
import org.example.vehicles_rental.entity.Payment;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.entity.Vehicle;
import org.example.vehicles_rental.enums.Role;
import org.example.vehicles_rental.exception.NotFoundException;
import org.example.vehicles_rental.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationSettingsRepository settingsRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    @Override
    public void notifyNewBooking(Booking booking) {

        NotificationSettings settings = getSettings();

        if (!settings.isNewBooking()) {
            return;
        }

        List<User> admins = userRepository.findByRole(Role.ADMIN);

        for (User admin : admins) {

            Notification notification = Notification.builder()
                    .userId(admin.getId())
                    .title("New Booking")
                    .message(
                            "Booking #" + booking.getId()
                                    + " was created by "
                                    + booking.getUser().getName()
                    )
                    .type("NEW_BOOKING")
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        }
    }
    @Override
    public void notifyPaymentReceived(Payment payment) {

        NotificationSettings settings = getSettings();

        if (!settings.isPaymentReceived()) {
            return;
        }

        List<User> admins =
                userRepository.findByRole(Role.ADMIN);

        for (User admin : admins) {

            Notification notification = Notification.builder()
                    .userId(admin.getId())
                    .title("Payment Received")
                    .message(
                            "Payment #" + payment.getId()
                                    + " has been received."
                    )
                    .type("PAYMENT_RECEIVED")
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        }
    }

    @Override
    public void notifyBookingCancellation(
            Booking booking) {

        NotificationSettings settings = getSettings();

        if (!settings.isBookingCancellation()) {
            return;
        }

        List<User> admins =
                userRepository.findByRole(Role.ADMIN);

        for (User admin : admins) {

            Notification notification = Notification.builder()
                    .userId(admin.getId())
                    .title("Booking Cancelled")
                    .message(
                            "Booking #" + booking.getId()
                                    + " has been cancelled."
                    )
                    .type("BOOKING_CANCELLATION")
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        }
    }
    @Override
    public void notifyNewUserRegistration(User user) {

        NotificationSettings settings = getSettings();

        if (!settings.isNewUserRegistration()) {
            return;
        }

        List<User> admins =
                userRepository.findByRole(Role.ADMIN);

        for (User admin : admins) {

            Notification notification = Notification.builder()
                    .userId(admin.getId())
                    .title("New User Registration")
                    .message(
                            user.getName()
                                    + " has registered."
                    )
                    .type("NEW_USER_REGISTRATION")
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        }
    }
    @Override
    public void notifyLowAvailability(Vehicle vehicle) {

        NotificationSettings settings = getSettings();

        if (!settings.isLowAvailabilityAlert()) {
            return;
        }

        List<User> admins =
                userRepository.findByRole(Role.ADMIN);

        for (User admin : admins) {

            Notification notification = Notification.builder()
                    .userId(admin.getId())
                    .title("Low Vehicle Availability")
                    .message(
                            "Vehicle "
                                    + vehicle.getId()
                                    + " has low availability."
                    )
                    .type("LOW_AVAILABILITY")
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        }
    }
    @Override
    public void notifyDailySummary() {

        NotificationSettings settings = getSettings();

        if (!settings.isDailySummaryReport()) {
            return;
        }

        List<User> admins =
                userRepository.findByRole(Role.ADMIN);

        for (User admin : admins) {

            Notification notification = Notification.builder()
                    .userId(admin.getId())
                    .title("Daily Summary")
                    .message(
                            "Your daily rental summary is ready."
                    )
                    .type("DAILY_SUMMARY")
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        }
    }
    private NotificationSettings getSettings() {

        return settingsRepository.findById(1L)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Notification settings not found"
                        )
                );
    }
    @Override
    public List<NotificationResponse> getNotifications(Long userId) {

        List<Notification> notifications =
                notificationRepository
                        .findByUserIdOrderByCreatedAtDesc(userId);

        return notifications.stream()
                .map(notification -> NotificationResponse.builder()
                        .id(notification.getId())
                        .title(notification.getTitle())
                        .message(notification.getMessage())
                        .type(notification.getType())
                        .isRead(notification.isRead())
                        .createdAt(notification.getCreatedAt())
                        .build()
                )
                .toList();
    }


}
