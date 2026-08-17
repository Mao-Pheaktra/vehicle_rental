package org.example.vehicles_rental.admin.setting.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_settings")
@Data
public class PaymentSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean cashEnabled;

    private boolean abaKhqrEnabled;

    private boolean cardEnabled;

    private boolean bakongEnabled;


}