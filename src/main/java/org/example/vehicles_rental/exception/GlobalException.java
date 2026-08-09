package org.example.vehicles_rental.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException{
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(EmaliAlreadyExists.class)
    public ResponseEntity<?> handleEmailAlreadyExists(EmaliAlreadyExists e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(IncorrectPassword.class)
    public ResponseEntity<?> handleIncorrectPassword(IncorrectPassword i){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(i.getMessage());
    }
    @ExceptionHandler(EmailAndPasswordNotMatch.class)
    public ResponseEntity<?> handleEmailAndPasswordNotMatch(EmailAndPasswordNotMatch e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
