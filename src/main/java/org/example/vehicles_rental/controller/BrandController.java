package org.example.vehicles_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.BrandRequest;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.example.vehicles_rental.dto.response.BrandResponse;
import org.example.vehicles_rental.service.BrandService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/brand")
public class BrandController {
    private final BrandService brandService;
    @PostMapping("/create")
    public ApiResponse<BrandResponse> create(@ModelAttribute BrandRequest brandRequest, @RequestParam("file") MultipartFile file)throws IOException {
        return new ApiResponse<>("create brand successfully",201, brandService.create(brandRequest,file));
    }
    @GetMapping("/getAll")
    public ApiResponse<List<BrandResponse>> getAll(){
        return new ApiResponse<>("get all brands Successfully",200,brandService.getAll());
    }
    @GetMapping("getById/{id}")
    public ApiResponse<BrandResponse> getById(@PathVariable Long id){
        return new ApiResponse<>("get By Id Successfully",200,brandService.getById(id));
    }
    @PutMapping("update/{id}")
    public ApiResponse<BrandResponse> update(@PathVariable Long id, @ModelAttribute BrandRequest brandRequest,@RequestParam("file") MultipartFile file)throws IOException {
        return new ApiResponse<>("update brand successfully",201,brandService.update(id,brandRequest,file));
    }
    @DeleteMapping("delete/{id}")
    public String delete(@PathVariable Long id){
        brandService.delete(id);
        return "delete brand successfully";
    }
}
