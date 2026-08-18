package org.example.vehicles_rental.admin.setting.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.vehicles_rental.admin.setting.enums.RequestStatus;
import org.example.vehicles_rental.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_requests")
@Data
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(nullable = false)
    private LocalDateTime requestAt;

    private LocalDateTime reviewedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewedBy")
    private User reviewedBy;

    private boolean autoApproved;
    private String requestedPassword;
}
