package org.example.vehicles_rental.exception;

public class PaymentAlreadyExists extends RuntimeException {

    public PaymentAlreadyExists(Long bookingId) {
        super("Payment already exists for booking: " + bookingId);
    }
}