package org.example.vehicles_rental.entity;

import jakarta.persistence.*;
<<<<<<< HEAD
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
=======
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
>>>>>>> origin/booking_api
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
<<<<<<< HEAD
@EntityListeners(AuditingEntityListener.class)
public class Vehicle {
=======
public class Vehicle {

>>>>>>> origin/booking_api
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String model;
    private Integer year;
<<<<<<< HEAD
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
=======
    @Column(name = "plate_number")
    private String plateNumber;
    private String transmission;
    @Column(name = "fuel_type")
    private String fuelType;
    private Integer seat;
    @Column(name = "price_per_day", precision = 10, scale = 2)
    private BigDecimal pricePerDay;
    private String status;
    private String description;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
>>>>>>> origin/booking_api
