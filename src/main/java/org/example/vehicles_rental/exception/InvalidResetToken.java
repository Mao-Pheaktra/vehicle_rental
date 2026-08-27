package org.example.vehicles_rental.exception;

public class InvalidResetToken extends RuntimeException{
    public InvalidResetToken(String message){
        super(message);
    }
}
