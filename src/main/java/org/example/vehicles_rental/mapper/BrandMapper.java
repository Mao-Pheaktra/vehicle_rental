package org.example.vehicles_rental.mapper;

import org.example.vehicles_rental.dto.response.BrandResponse;
import org.example.vehicles_rental.entity.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {
    public BrandResponse toBrandResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .brand_name(brand.getBrand_name())
                .logo(brand.getLogo())
                .created_at(brand.getCreated_at())
                .updated_at(brand.getUpdated_at())
                .build();
    }
}
