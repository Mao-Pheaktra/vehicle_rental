package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.BrandRequest;
import org.example.vehicles_rental.dto.response.BrandResponse;
import org.example.vehicles_rental.entity.Brand;
import org.example.vehicles_rental.exception.NotFoundException;
import org.example.vehicles_rental.mapper.BrandMapper;
import org.example.vehicles_rental.repository.BrandRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    @Override
    public BrandResponse create(BrandRequest brandRequest, MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String fileName = file.getOriginalFilename();
        String fileUrl = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get("upload");
        String imageUrl = "http://localhost:8080/upload/" + fileUrl;
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        Files.copy(file.getInputStream(),path.resolve(fileUrl));
        Brand  brand = Brand.builder()
                .brand_name(brandRequest.getBrand_name())
                .logo(imageUrl)
                .build();
        brand=brandRepository.save(brand);
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    public List<BrandResponse> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<Brand> brands = brandRepository.findAll();
        List<BrandResponse> brandResponses = new ArrayList<>();
        for(Brand brand1 : brands){
            BrandResponse brandResponse = BrandResponse.builder()
                    .id(brand1.getId())
                    .brand_name(brand1.getBrand_name())
                    .logo(brand1.getLogo())
                    .created_at(brand1.getCreated_at())
                    .updated_at(brand1.getUpdated_at())
                    .build();
            brandResponses.add(brandResponse);
        }
        return brandResponses;
    }

    @Override
    public BrandResponse getById(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Brand brand = brandRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Brand Id " +id+ " Not Found"));
        BrandResponse brandResponse = BrandResponse.builder()
                .id(brand.getId())
                .brand_name(brand.getBrand_name())
                .logo(brand.getLogo())
                .created_at(brand.getCreated_at())
                .updated_at(brand.getUpdated_at())
                .build();
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    public BrandResponse update(Long id, BrandRequest brandRequest, MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String fileName = file.getOriginalFilename();
        String fileUrl = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get("upload");
        String imageUrl = "http://localhost:8080/upload/" + fileUrl;
        Files.copy(file.getInputStream(),path.resolve(fileUrl));

        Brand brand = brandRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Brand Id " +id+ " Not Found"));
                brand.setBrand_name(brandRequest.getBrand_name());
                brand.setLogo(imageUrl);
        brand=brandRepository.save(brand);
        return brandMapper.toBrandResponse(brand);
    }

    @Override
    public void delete(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Brand brand = brandRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Brand Id " +id+ " Not Found"));
        brandRepository.delete(brand);
    }
}
