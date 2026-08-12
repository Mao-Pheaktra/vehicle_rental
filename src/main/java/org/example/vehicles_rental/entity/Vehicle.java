package org.example.vehicles_rental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.vehicles_rental.enums.Status;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String model;
    private Integer year;
    private String plate_number;
    private String transmission;
    private String fuel_type;
    private Integer seat;
    private Long price_per_day;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String description;

    @CreatedDate
    @Column(name = "create_at",updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "vehicle",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Vehicle_Image> vehicleImages;

}