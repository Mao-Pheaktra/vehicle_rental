package org.example.vehicles_rental.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_methods")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "method_name", nullable = false)
    private String methodName;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private String PmStatus;
}