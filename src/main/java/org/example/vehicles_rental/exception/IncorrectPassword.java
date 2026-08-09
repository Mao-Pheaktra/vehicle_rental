package org.example.vehicles_rental.exception;

public class IncorrectPassword extends RuntimeException{
    public IncorrectPassword (String message){
        super(message);
    }
}
