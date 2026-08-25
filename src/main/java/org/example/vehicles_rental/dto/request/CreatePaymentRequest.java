package org.example.vehicles_rental.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentRequest {
    @NotNull(message = "Booking ID is required")
    private Long bookingId;
    @NotNull(message = "Payment method ID is required")
    private Long paymentMethodId;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;
    @NotNull(message = "Currency is required")
    private String currency;
}