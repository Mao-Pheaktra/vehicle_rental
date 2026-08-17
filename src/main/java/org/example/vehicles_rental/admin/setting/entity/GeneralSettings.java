package org.example.vehicles_rental.admin.setting.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "general_settings")
@Data
public class GeneralSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String businessName;

    private String contactEmail;

    private String phoneNumber;

    private String address;

    private String currency;

    private String timezone;
}