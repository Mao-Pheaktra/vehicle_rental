package org.example.vehicles_rental.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.vehicles_rental.enums.BrandStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandRequest {
    private String name;
    private String logo;
    private String description;
    private BrandStatus status;
    private List<Long> categoryIds;
}