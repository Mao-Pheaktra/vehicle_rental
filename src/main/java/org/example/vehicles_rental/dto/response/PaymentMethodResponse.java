package org.example.vehicles_rental.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodResponse {
    private Long id;
    private String methodName;
    private String description;
    private String PmStatus;
}
