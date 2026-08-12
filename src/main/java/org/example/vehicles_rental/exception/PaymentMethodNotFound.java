package org.example.vehicles_rental.exception;

public class PaymentMethodNotFound extends RuntimeException {
    public PaymentMethodNotFound(Long id) {
        super("Payment method not found with id: " + id);
    }
    public PaymentMethodNotFound(String message) {
        super(message);
    }
}