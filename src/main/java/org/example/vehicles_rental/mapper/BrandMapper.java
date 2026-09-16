package org.example.vehicles_rental.mapper;

import org.example.vehicles_rental.dto.response.BrandResponse;
import org.example.vehicles_rental.entity.Brand;
import org.example.vehicles_rental.entity.Categories;
import org.example.vehicles_rental.enums.BrandStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrandMapper {

    public BrandResponse toBrandResponse(Brand brand) {
        if (brand == null) {
            return null;
        }

        List<String> categoryNames = (brand.getCategories() != null)
                ? brand.getCategories().stream().map(Categories::getCategoryName).toList()
                : List.of();

        int vehicleCount = (brand.getVehicles() != null) ? brand.getVehicles().size() : 0;

        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getBrandName())
                .logo(brand.getLogo())
                .description("Manufacturer profile for " + brand.getBrandName())
                .status(brand.getStatus() != null ? brand.getStatus() : BrandStatus.ACTIVE)
                .vehicleCount(vehicleCount)
                .categories(categoryNames)
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .build();
    }
}