package org.example.vehicles_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.CategoryRequest;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.example.vehicles_rental.dto.response.CategoryResponse;
import org.example.vehicles_rental.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/create")
    public ApiResponse<CategoryResponse> create(@RequestBody CategoryRequest categoryRequest){
        return new ApiResponse<>("create category successfully",201,categoryService.create(categoryRequest));
    }
    @GetMapping("/getAll")
    public ApiResponse<List<CategoryResponse>> getAll(){
        return new ApiResponse<>("get all categories successfully",200,categoryService.getAll());
    }
    @GetMapping("/getById/{id}")
    public ApiResponse<CategoryResponse> getById(@PathVariable Long id){
        return new ApiResponse<>("get category by id successfully",200,categoryService.getById(id));
    }
    @PutMapping("/update/{id}")
    public ApiResponse<CategoryResponse> update(@PathVariable Long id,@RequestBody CategoryRequest categoryRequest){
        return new ApiResponse<>("update category successfully",200,categoryService.update(id,categoryRequest));
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        categoryService.delete(id);
        return "delete category successfully";
    }
}
