package org.example.vehicles_rental.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalException{
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(EmaliAlreadyExists.class)
    public ResponseEntity<?> handleEmailAlreadyExists(EmaliAlreadyExists e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(IncorrectPassword.class)
    public ResponseEntity<?> handleIncorrectPassword(IncorrectPassword e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(EmailAndPasswordNotMatch.class)
    public ResponseEntity<?> handleEmailAndPasswordNotMatch(
            EmailAndPasswordNotMatch e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(EmailVerify.class)
    public ResponseEntity<?> handleEmailVerify(EmailVerify e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidOTP.class)
    public ResponseEntity<?> handleInvalidOTP(InvalidOTP e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
    @ExceptionHandler(OtpExpireException.class)
    public ResponseEntity<?> handleOtpExpired(OtpExpireException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", 400,
                        "message", ex.getMessage()
                ));
    }
    @ExceptionHandler(TooManyRequestException.class)
    public ResponseEntity<?> handleTooManyRequest(TooManyRequestException e) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(e.getMessage());
    }

}
