package org.example.vehicles_rental.admin.setting.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "security_settings")
@Data
public class SecuritySettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean twoFactorAuthentication;

    private Integer sessionTimeoutMinutes;
}