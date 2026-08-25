package org.example.vehicles_rental.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.vehicles_rental.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BakongPaymentResponse {
    private Long paymentId;
    private Long bookingId;
    private BigDecimal amount;
    private String currency;
    private String transactionId;
    private String qr;
    private String md5;
    private LocalDateTime expiresAt;
    private LocalDateTime paidAt;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
}