package org.example.vehicles_rental.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.vehicles_rental.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(
            name = "amount",
            precision = 10,
            scale = 2,
            nullable = false
    )
    private BigDecimal amount;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "bakong_account")
    private String bakongAccount;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "qr", length = 2000)
    private String qr;

    @Column(name = "md5", length = 32)
    private String md5;

    @Column(name = "bakong_transaction_hash")
    private String bakongTransactionHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @PrePersist
    protected void onCreate() {

        this.createdAt = LocalDateTime.now();

        if (this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.PENDING;
        }

        if (this.paymentDate == null) {
            this.paymentDate = LocalDate.now();
        }
    }
}