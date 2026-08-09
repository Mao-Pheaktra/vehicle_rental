package org.example.vehicles_rental.exception;

public class EmailAndPasswordNotMatch extends RuntimeException{
    public EmailAndPasswordNotMatch (String message){
        super(message);
    }
}
