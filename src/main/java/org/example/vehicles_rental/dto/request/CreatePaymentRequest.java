package org.example.vehicles_rental.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentRequest {
    private Long bookingId;
    private Long paymentMethodId;
    private BigDecimal amount;
    private String currency;
    private String bakongAccount;
}