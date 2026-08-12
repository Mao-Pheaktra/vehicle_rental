package org.example.vehicles_rental.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Long bookingId;
    private Long paymentMethodId;
    private BigDecimal amount;
    private String transactionId;
    private String PStatus;
    private LocalDate paymentDate;
}