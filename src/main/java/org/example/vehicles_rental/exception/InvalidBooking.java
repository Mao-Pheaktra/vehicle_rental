package org.example.vehicles_rental.exception;

public class InvalidBooking extends RuntimeException {
    public InvalidBooking(String message) {
        super(message);
    }
}