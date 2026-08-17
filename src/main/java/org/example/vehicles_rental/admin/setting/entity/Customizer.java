package org.example.vehicles_rental.admin.setting.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_customize")
public class Customizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logo;

    private String heroImage;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String buttonText;

    private String buttonLink;

    // Admin who last updated the settings
    private Long updatedBy;

    private LocalDateTime updatedAt;
}
