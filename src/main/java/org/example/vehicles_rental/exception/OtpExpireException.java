package org.example.vehicles_rental.exception;

public class OtpExpireException extends RuntimeException{
    public OtpExpireException (String message){
        super(message);
    }
}
