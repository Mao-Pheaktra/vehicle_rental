package org.example.vehicles_rental.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodRequest {
    private String methodName;
    private String description;
    private String PmStatus;
}