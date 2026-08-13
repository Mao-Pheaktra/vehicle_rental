package org.example.vehicles_rental.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.vehicles_rental.dto.response.PaymentResponse;
import org.example.vehicles_rental.enums.PaymentMethodName;
import org.example.vehicles_rental.enums.PaymentMethodStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodRequest {
    private PaymentMethodName paymentMethodName;
    private String description;
    private PaymentMethodStatus status;
}