package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.BrandRequest;
import org.example.vehicles_rental.dto.response.BrandResponse;
import org.example.vehicles_rental.entity.Brand;
import org.example.vehicles_rental.enums.BrandStatus;
import org.example.vehicles_rental.exception.NotFoundException;
import org.example.vehicles_rental.mapper.BrandMapper;
import org.example.vehicles_rental.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    public BrandResponse create(BrandRequest brandRequest, MultipartFile file) throws IOException {
        String imageUrl = null;
        if (file != null && !file.isEmpty()) {
            imageUrl = cloudinaryService.uploadBrandImage(file);
        } else if (brandRequest.getLogo() != null && !brandRequest.getLogo().isBlank()) {
            imageUrl = brandRequest.getLogo();
        }

        Brand brand = Brand.builder()
                .brandName(brandRequest.getName())
                .logo(imageUrl)
                .status(brandRequest.getStatus() != null ? brandRequest.getStatus() : BrandStatus.ACTIVE)
                .build();

        brand = brandRepository.save(brand);
        return mapToBrandResponse(brand);
    }

    @Override
    public List<BrandResponse> getAll() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(this::mapToBrandResponse)
                .toList();
    }

    @Override
    public BrandResponse getById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand Id " + id + " Not Found"));
        return mapToBrandResponse(brand);
    }

    @Override
    public BrandResponse update(Long id, BrandRequest brandRequest, MultipartFile file) throws IOException {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand Id " + id + " Not Found"));

        if (brandRequest.getName() != null && !brandRequest.getName().isBlank()) {
            brand.setBrandName(brandRequest.getName());
        }

        if (brandRequest.getStatus() != null) {
            brand.setStatus(brandRequest.getStatus());
        }

        if (file != null && !file.isEmpty()) {
            String imageUrl = cloudinaryService.uploadBrandImage(file);
            brand.setLogo(imageUrl);
        } else if (brandRequest.getLogo() != null && !brandRequest.getLogo().isBlank()) {
            brand.setLogo(brandRequest.getLogo());
        }

        brand = brandRepository.save(brand);
        return mapToBrandResponse(brand);
    }

    @Override
    public void delete(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand Id " + id + " Not Found"));
        brandRepository.delete(brand);
    }

    /* Mapper Helper */
    private BrandResponse mapToBrandResponse(Brand brand) {
        List<String> categoryNames = (brand.getCategories() != null)
                ? brand.getCategories().stream().map(c -> c.getCategoryName()).toList()
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