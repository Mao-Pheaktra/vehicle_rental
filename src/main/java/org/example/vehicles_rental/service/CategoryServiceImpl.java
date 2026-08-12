package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.CategoryRequest;
import org.example.vehicles_rental.dto.response.CategoryResponse;
import org.example.vehicles_rental.entity.Categories;
import org.example.vehicles_rental.exception.NotFoundException;
import org.example.vehicles_rental.mapper.CategoryMapper;
import org.example.vehicles_rental.repository.CategoryRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper  categoryMapper;
    @Override
    public CategoryResponse create(CategoryRequest categoryRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Categories categories = Categories.builder()
                .category_name(categoryRequest.getCategory_name())
                .description(categoryRequest.getDescription())
                .build();
        Categories savedCategories = categoryRepository.save(categories);
        return categoryMapper.tocategoryResponse(savedCategories);
    }

    @Override
    public List<CategoryResponse> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<Categories> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Categories category : categories) {
          categoryResponses.add(categoryMapper.tocategoryResponse(category));
        }
        return categoryResponses;
    }

    @Override
    public CategoryResponse getById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Categories categories = categoryRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Category Id " +id+ " Not Found"));
        return categoryMapper.tocategoryResponse(categories);
    }

    @Override
    public CategoryResponse update(Long id,CategoryRequest categoryRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Categories categories = categoryRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Category Id " +id+ " Not Found"));
        categories.setCategory_name(categoryRequest.getCategory_name());
        categories.setDescription(categoryRequest.getDescription());
        categoryRepository.save(categories);
        return categoryMapper.tocategoryResponse(categories);

    }

    @Override
    public void delete(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Categories categories = categoryRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Category Id " +id+ " Not Found"));
        categoryRepository.deleteById(categories.getId());
    }
}
