package org.example.vehicles_rental.exception;

public class BookingNotFound extends RuntimeException {
    public BookingNotFound(Long id) {
        super("Booking not found with id: " + id);
    }
}
