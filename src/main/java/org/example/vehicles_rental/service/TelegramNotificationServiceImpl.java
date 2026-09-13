package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.entity.Booking;
import org.example.vehicles_rental.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramNotificationServiceImpl
        implements TelegramNotificationService {

    private final TelegramBotService telegramBotService;


    // =========================================================
    // NEW BOOKING
    // =========================================================

    @Override
    public void notifyNewBooking(Booking booking) {

        System.out.println("===== TELEGRAM NEW BOOKING START =====");

        if (booking == null) {
            System.out.println("Booking is NULL");
            return;
        }

        System.out.println("Booking ID: " + booking.getId());

        if (booking.getUser() == null) {
            System.out.println("Booking user is NULL");
            return;
        }

        System.out.println("User ID: " + booking.getUser().getId());
        System.out.println("User Name: " + booking.getUser().getName());
        System.out.println("Telegram Chat ID: " +
                booking.getUser().getTelegramChatId());

        String customerName = booking.getUser().getName();
        String vehicleName = booking.getVehicle() != null
                ? booking.getVehicle().getName()
                : "Unknown";

        String message =
                "🚗 NEW BOOKING\n\n" +
                        "📋 Booking ID: #" + booking.getId() + "\n" +
                        "👤 Customer: " + customerName + "\n" +
                        "🚘 Vehicle: " + vehicleName + "\n\n" +
                        "📅 Pickup Date: " + booking.getPickupDate() + "\n" +
                        "📅 Return Date: " + booking.getReturnDate() + "\n" +
                        "🕒 Rental Days: " + booking.getTotalDays() + "\n\n" +
                        "💵 Total Price: $" + booking.getTotalPrice() + "\n" +
                        "📌 Status: " + booking.getStatus() + "\n\n" +
                        "Your booking has been created successfully.";

        System.out.println("Sending Telegram message...");

        sendToUser(booking.getUser(), message);

        System.out.println("===== TELEGRAM NEW BOOKING END =====");
    }


    // =========================================================
    // PAYMENT SUCCESS
    // =========================================================

    @Override
    public void notifyPaymentSuccess(Booking booking) {

        String message =
                " Payment Successful\n\n" +
                        "Booking ID: #" + booking.getId() + "\n" +
                        "Status: PAID\n\n" +
                        "Your payment has been received successfully.";

        sendToUser(
                booking.getUser(),
                message
        );
    }


    // =========================================================
    // BOOKING APPROVED
    // =========================================================

    @Override
    public void notifyBookingApproved(Booking booking) {

        String message =
                " Booking Approved\n\n" +
                        "Booking ID: #" + booking.getId() + "\n" +
                        "Status: APPROVED\n\n" +
                        "Your vehicle rental booking has been approved.";

        sendToUser(
                booking.getUser(),
                message
        );
    }


    // =========================================================
    // BOOKING REJECTED
    // =========================================================

    @Override
    public void notifyBookingRejected(Booking booking) {

        String message =
                " Booking Rejected\n\n" +
                        "Booking ID: #" + booking.getId() + "\n" +
                        "Status: REJECTED\n\n" +
                        "Unfortunately, your vehicle rental booking has been rejected.";

        sendToUser(
                booking.getUser(),
                message
        );
    }


    // =========================================================
    // BOOKING CANCELLED
    // =========================================================

    @Override
    public void notifyBookingCancelled(Booking booking) {

        String message =
                " Booking Cancelled\n\n" +
                        "Booking ID: #" + booking.getId() + "\n" +
                        "Status: CANCELLED\n\n" +
                        "Your vehicle rental booking has been cancelled.";

        sendToUser(
                booking.getUser(),
                message
        );
    }


    // =========================================================
    // RENTAL STARTED
    // =========================================================

    @Override
    public void notifyRentalStarted(Booking booking) {

        String message =
                " Rental Started\n\n" +
                        "Booking ID: #" + booking.getId() + "\n" +
                        "Status: RENTED\n\n" +
                        "Your vehicle rental has started. Have a safe trip!";

        sendToUser(
                booking.getUser(),
                message
        );
    }


    // =========================================================
    // RENTAL COMPLETED
    // =========================================================

    @Override
    public void notifyRentalCompleted(Booking booking) {

        String message =
                " Rental Completed\n\n" +
                        "Booking ID: #" + booking.getId() + "\n" +
                        "Status: COMPLETED\n\n" +
                        "Your vehicle rental has been completed. Thank you for using our service.";

        sendToUser(
                booking.getUser(),
                message
        );
    }


    // =========================================================
    // COMMON TELEGRAM METHOD
    // =========================================================

    private void sendToUser(User user, String message) {

        if (user == null) {
            return;
        }

        String telegramChatId = user.getTelegramChatId();

        // User has not connected Telegram
        if (telegramChatId == null ||
                telegramChatId.isBlank()) {

            return;
        }

        Long chatId;

        try {

            chatId = Long.parseLong(telegramChatId);

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid Telegram Chat ID for user: "
                            + user.getId()
            );

            return;
        }

        try {

            telegramBotService.sendMessage(
                    chatId,
                    message
            );

            System.out.println(
                    "Telegram notification sent to user: "
                            + user.getId()
            );

        } catch (Exception e) {

            System.out.println(
                    "Failed to send Telegram notification: "
                            + e.getMessage()
            );
        }
    }
}