package org.example.vehicles_rental.service;

import org.example.vehicles_rental.dto.request.CategoryRequest;
import org.example.vehicles_rental.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest categoryRequest);
    List<CategoryResponse> getAll();
    CategoryResponse getById(Long id);
    CategoryResponse update(Long id,CategoryRequest categoryRequest);
    void delete(Long id);
}
