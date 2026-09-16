package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.Payment;
import org.example.vehicles_rental.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBookingId(Long bookingId);
    Optional<Payment> findFirstByBookingIdOrderByCreatedAtDesc(Long bookingId);
    Optional<Payment> findFirstByBookingIdAndPaymentStatusOrderByCreatedAtDesc(
            Long bookingId,
            PaymentStatus paymentStatus);
    Optional<Payment> findByTransactionId(String transactionId);
    boolean existsByBookingId(Long bookingId);
    Optional<Payment> findByMd5(String md5);

}
