package org.example.vehicles_rental.admin.setting.service;

import org.example.vehicles_rental.admin.setting.dto.response.NotificationResponse;
import org.example.vehicles_rental.entity.Booking;
import org.example.vehicles_rental.entity.Payment;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.entity.Vehicle;

import java.util.List;

public interface NotificationService {

    void notifyNewBooking(Booking booking);

    void notifyPaymentReceived(Payment payment);

    void notifyBookingCancellation(Booking booking);

    void notifyNewUserRegistration(User user);

    void notifyLowAvailability(Vehicle vehicle);

    void notifyDailySummary();

    List<NotificationResponse> getNotifications(Long userId);
}
