package org.example.vehicles_rental.exception;

public class TooManyRequestException extends RuntimeException{
    public TooManyRequestException(String message) {
        super(message);
    }
}
