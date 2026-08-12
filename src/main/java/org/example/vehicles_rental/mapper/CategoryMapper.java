package org.example.vehicles_rental.mapper;

import org.example.vehicles_rental.dto.response.CategoryResponse;
import org.example.vehicles_rental.entity.Categories;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponse tocategoryResponse(Categories category){
        return CategoryResponse.builder()
                .id(category.getId())
                .category_name(category.getCategory_name())
                .description(category.getDescription())
                .created_at(category.getCreated_at())
                .updated_at(category.getUpdated_at())
                .build();
    }

}
