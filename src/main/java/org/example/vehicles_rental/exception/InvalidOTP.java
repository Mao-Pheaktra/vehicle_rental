package org.example.vehicles_rental.exception;

public class InvalidOTP extends RuntimeException{
    public InvalidOTP(String message){
        super(message);
    }
}
