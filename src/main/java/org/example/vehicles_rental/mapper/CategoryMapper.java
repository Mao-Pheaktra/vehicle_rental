package org.example.vehicles_rental.mapper;

import org.example.vehicles_rental.dto.response.CategoryResponse;
import org.example.vehicles_rental.entity.Categories;
import org.example.vehicles_rental.enums.CategoryStatus;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toCategoryResponse(Categories category) {
        if (category == null) {
            return null;
        }

        int vehicleCount = (category.getVehicles() != null) ? category.getVehicles().size() : 0;

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getCategoryName())
                .description(category.getDescription())
                .status(category.getStatus() != null ? category.getStatus() : CategoryStatus.ACTIVE)
                .vehicleCount(vehicleCount)
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}