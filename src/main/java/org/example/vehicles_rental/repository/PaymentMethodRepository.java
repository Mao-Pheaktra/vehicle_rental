package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

}