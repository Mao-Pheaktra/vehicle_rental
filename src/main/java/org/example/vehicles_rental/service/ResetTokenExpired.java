package org.example.vehicles_rental.service;

public class ResetTokenExpired extends RuntimeException{
    public ResetTokenExpired(String message){
        super(message);
    }
}
