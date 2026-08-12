package org.example.vehicles_rental.service;

import org.example.vehicles_rental.dto.request.BrandRequest;
import org.example.vehicles_rental.dto.response.BrandResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BrandService {
    BrandResponse create(BrandRequest request, MultipartFile file)throws IOException;
    List<BrandResponse> getAll();
    BrandResponse getById(Long id);
    BrandResponse update(Long id,BrandRequest brandRequest,MultipartFile file)throws IOException;
    void delete(Long id);

}
