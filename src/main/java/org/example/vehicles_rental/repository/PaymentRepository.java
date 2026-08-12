package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBookingId(Long bookingId);
    List<Payment> findByPaymentStatus(String paymentStatus);
}