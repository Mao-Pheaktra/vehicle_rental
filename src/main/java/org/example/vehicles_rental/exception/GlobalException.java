package org.example.vehicles_rental.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalException{
    @ExceptionHandler({NotFoundException.class, UserNotFound.class, VehicleNotFound.class})
    public ResponseEntity<?> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", 404,
                        "message", e.getMessage()
                ));
    }

    @ExceptionHandler(EmaliAlreadyExists.class)
    public ResponseEntity<?> handleEmailAlreadyExists(EmaliAlreadyExists e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "status", 409,
                        "message", e.getMessage()
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrity(DataIntegrityViolationException e) {
        String message = e.getMostSpecificCause() == null
                ? e.getMessage()
                : e.getMostSpecificCause().getMessage();
        String responseMessage = message != null && message.toLowerCase().contains("email")
                ? "This email is already registered"
                : "Could not save this data. Please check that the selected item exists.";

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "status", 409,
                        "message", responseMessage
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "status", 401,
                        "message", e.getMessage()
                ));
    }

    @ExceptionHandler(EmailAndPasswordNotMatch.class)
    public ResponseEntity<?> handleEmailAndPasswordNotMatch(
            EmailAndPasswordNotMatch e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "status", 401,
                        "message", e.getMessage()
                ));
    }

    @ExceptionHandler(EmailVerify.class)
    public ResponseEntity<?> handleEmailVerify(EmailVerify e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "status", 401,
                        "message", e.getMessage()
                ));
    }

    @ExceptionHandler(InvalidOTP.class)
    public ResponseEntity<?> handleInvalidOTP(InvalidOTP e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", 400,
                        "message", e.getMessage()
                ));
    }
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

    @ExceptionHandler({BookingNotFound.class, PaymentMethodNotFound.class, PaymentNotFound.class})
    public ResponseEntity<?> handlePaymentNotFound(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", 404,
                        "message", e.getMessage()
                ));
    }

    @ExceptionHandler(PaymentAlreadyExists.class)
    public ResponseEntity<?> handlePaymentAlreadyExists(PaymentAlreadyExists e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "status", 409,
                        "message", e.getMessage()
                ));
    }

    @ExceptionHandler({IllegalArgumentException.class, InvalidBooking.class, PaymentFailed.class})
    public ResponseEntity<?> handlePaymentBadRequest(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", 400,
                        "message", e.getMessage()
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "status", 403,
                        "message", e.getMessage()
                ));
    }

}
