package org.example.vehicles_rental.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.vehicles_rental.enums.BrandStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandResponse {
    private Long id;
    private String name;
    private String logo;
    private String description;
    private BrandStatus status;
    private Integer vehicleCount;
    private List<String> categories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}