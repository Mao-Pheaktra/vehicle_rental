package org.example.vehicles_rental.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.vehicles_rental.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_payment_bakong_md5",
                        columnNames = "md5"
                ),
                @UniqueConstraint(
                        name = "uk_payment_bakong_transaction_hash",
                        columnNames = "bakong_transaction_hash"
                )
        }
)
@Getter
@Setter
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

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(length = 100)
    private String transactionId;

    @Column(length = 100)
    private String bakongAccount;

    @Column(length = 10)
    private String currency;

    @Column(length = 2000)
    private String qr;

    @Column(length = 100, unique = true)
    private String md5;

    @Column(name = "bakong_transaction_hash", length = 255, unique = true)
    private String bakongTransactionHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime paidAt;

    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.PENDING;
        }
    }
}