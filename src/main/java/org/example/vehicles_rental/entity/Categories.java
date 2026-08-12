package org.example.vehicles_rental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category_name;
    private String description;

    @CreatedDate
    @Column(name = "create_at",updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updated_at;

    @ManyToMany(mappedBy = "categories")
    private List<Brand> brands;


}
