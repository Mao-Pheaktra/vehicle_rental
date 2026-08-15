package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.Payment;
import org.example.vehicles_rental.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface PaymentDashboardRepository extends JpaRepository<Payment, Long> {

    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.paymentStatus = :status
            """)
    BigDecimal sumAmountByStatus(
            @Param("status") PaymentStatus status
    );

    @Query("""
            SELECT COUNT(p)
            FROM Payment p
            WHERE p.paymentStatus = :status
            """)
    long countByStatus(
            @Param("status") PaymentStatus status
    );
}