package org.example.vehicles_rental.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    // 404 NOT FOUND
    @ExceptionHandler({
            NotFoundException.class,
            PaymentNotFound.class,
            BookingNotFound.class,
            PaymentMethodNotFound.class,
            VehicleNotFound.class,
            UserNotFound.class
    })
    public ResponseEntity<?> handleNotFound(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", 404,
                        "message", e.getMessage()
                ));
    }

    // 409 CONFLICT
    @ExceptionHandler({
            EmailAlreadyExists.class,
            DuplicatePaymentMethod.class,
            PaymentAlreadyExists.class
    })
    public ResponseEntity<?> handleConflict(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "status", 409,
                        "message", e.getMessage()
                ));
    }

    // 400 BAD REQUEST
    @ExceptionHandler({
            IllegalArgumentException.class,
            InvalidOTP.class,
            PaymentFailed.class,
            InvalidBooking.class
    })
    public ResponseEntity<?> handleBadRequest(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", 400,
                        "message", e.getMessage()
                ));
    }

    // 401 UNAUTHORIZED
    @ExceptionHandler(IncorrectPassword.class)
    public ResponseEntity<?> handleIncorrectPassword(IncorrectPassword e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "status", 401,
                        "message", e.getMessage()
                ));
    }

    @ExceptionHandler(EmailAndPasswordNotMatch.class)
    public ResponseEntity<?> handleEmailAndPasswordNotMatch(
            EmailAndPasswordNotMatch e
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "status", 401,
                        "message", e.getMessage()
                ));
    }

    @ExceptionHandler(EmailVerify.class)
    public ResponseEntity<?> handleEmailVerify(EmailVerify e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "status", 401,
                        "message", e.getMessage()
                ));
    }

    // OTP EXPIRED
    @ExceptionHandler(OtpExpireException.class)
    public ResponseEntity<?> handleOtpExpired(OtpExpireException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", 400,
                        "message", e.getMessage()
                ));
    }

    // 429 TOO MANY REQUESTS
    @ExceptionHandler(TooManyRequestException.class)
    public ResponseEntity<?> handleTooManyRequest(
            TooManyRequestException e
    ) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of(
                        "status", 429,
                        "message", e.getMessage()
                ));
    }

    // 500 INTERNAL SERVER ERROR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "status", 500,
                        "message", "An unexpected error occurred"
                ));
    }
}