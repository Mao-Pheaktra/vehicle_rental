package org.example.vehicles_rental.exception;

public class ResetTokenExpired extends RuntimeException{
    public ResetTokenExpired(String message){
        super(message);
    }
}
