package org.example.vehicles_rental.dto.response;

import lombok.*;
import org.example.vehicles_rental.enums.PaymentMethodName;
import org.example.vehicles_rental.enums.PaymentMethodStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodResponse {
    private Long id;
    private PaymentMethodName paymentMethodName;
    private String description;
    private PaymentMethodStatus status;
}
