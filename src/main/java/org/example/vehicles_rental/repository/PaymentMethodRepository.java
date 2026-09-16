package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.PaymentMethod;
import org.example.vehicles_rental.enums.PaymentMethodName;
import org.example.vehicles_rental.enums.PaymentMethodStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByPaymentMethodNameAndStatus(
            PaymentMethodName paymentMethodName,
            PaymentMethodStatus status
    );
}
