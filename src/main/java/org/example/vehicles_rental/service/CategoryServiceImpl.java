package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.CategoryRequest;
import org.example.vehicles_rental.dto.response.CategoryResponse;
import org.example.vehicles_rental.entity.Categories;
import org.example.vehicles_rental.enums.CategoryStatus;
import org.example.vehicles_rental.exception.NotFoundException;
import org.example.vehicles_rental.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse create(CategoryRequest categoryRequest) {
        Categories categories = Categories.builder()
                .categoryName(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .status(categoryRequest.getStatus() != null ? categoryRequest.getStatus() : CategoryStatus.ACTIVE)
                .build();

        Categories savedCategories = categoryRepository.save(categories);
        return mapToCategoryResponse(savedCategories);
    }

    @Override
    public List<CategoryResponse> getAll() {
        List<Categories> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::mapToCategoryResponse)
                .toList();
    }

    @Override
    public CategoryResponse getById(Long id) {
        Categories categories = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Id " + id + " Not Found"));
        return mapToCategoryResponse(categories);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest categoryRequest) {
        Categories categories = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Id " + id + " Not Found"));

        if (categoryRequest.getName() != null && !categoryRequest.getName().isBlank()) {
            categories.setCategoryName(categoryRequest.getName());
        }

        if (categoryRequest.getDescription() != null) {
            categories.setDescription(categoryRequest.getDescription());
        }

        if (categoryRequest.getStatus() != null) {
            categories.setStatus(categoryRequest.getStatus());
        }

        Categories updatedCategory = categoryRepository.save(categories);
        return mapToCategoryResponse(updatedCategory);
    }

    @Override
    public void delete(Long id) {
        Categories categories = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Id " + id + " Not Found"));
        categoryRepository.delete(categories);
    }

    /* Mapper Helper */
    private CategoryResponse mapToCategoryResponse(Categories category) {
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