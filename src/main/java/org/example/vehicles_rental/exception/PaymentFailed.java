package org.example.vehicles_rental.exception;

public class PaymentFailed extends RuntimeException {
    public PaymentFailed(String message) {
        super(message);
    }
}