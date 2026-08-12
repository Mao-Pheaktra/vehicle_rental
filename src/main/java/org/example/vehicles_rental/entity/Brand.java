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
public class Brand {
    @Id
    @GeneratedValue
    private Long id;
    private String brand_name;
    private String logo;

    @CreatedDate
    @Column(name = "create_at", updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updated_at;

    @ManyToMany
    @JoinTable(name = "brand_category",joinColumns=@JoinColumn(name = "brand_id"),inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Categories> categories;
}
