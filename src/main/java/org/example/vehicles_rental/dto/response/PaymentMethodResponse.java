package org.example.vehicles_rental.dto.response;

import lombok.*;
import org.example.vehicles_rental.enums.PaymentMethodName;
import org.example.vehicles_rental.enums.PaymentMethodStatus;
import org.example.vehicles_rental.enums.PaymentStatus;

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
