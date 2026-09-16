package org.example.vehicles_rental.service;

import org.example.vehicles_rental.entity.Booking;

public interface TelegramNotificationService {

    void notifyNewBooking(Booking booking);

    void notifyPaymentSuccess(Booking booking);

    void notifyBookingApproved(Booking booking);

    void notifyBookingRejected(Booking booking);

    void notifyBookingCancelled(Booking booking);

    void notifyRentalStarted(Booking booking);

    void notifyRentalCompleted(Booking booking);
}