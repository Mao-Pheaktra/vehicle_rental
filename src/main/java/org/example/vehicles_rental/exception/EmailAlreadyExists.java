package org.example.vehicles_rental.exception;

public class EmailAlreadyExists extends RuntimeException{
    public EmailAlreadyExists(String message){
        super(message);
    }
}
