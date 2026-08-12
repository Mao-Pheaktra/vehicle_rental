package org.example.vehicles_rental.exception;

public class DuplicatePaymentMethod extends RuntimeException {
    public DuplicatePaymentMethod(String methodName) {
        super("Payment method already exists: " + methodName);
    }
}