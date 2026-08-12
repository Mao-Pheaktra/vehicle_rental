package org.example.vehicles_rental.exception;

public class PaymentNotFound extends RuntimeException {
    public PaymentNotFound(Long id) {
        super("Payment not found with id: " + id);
    }
    public PaymentNotFound(String message) {
        super(message);
    }
}