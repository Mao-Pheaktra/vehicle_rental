package org.example.vehicles_rental.service;

import org.example.vehicles_rental.dto.request.Vehicle_imageRequest;
import org.example.vehicles_rental.dto.response.Vehicle_imageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface Vehicle_ImageService {
    Vehicle_imageResponse create(Vehicle_imageRequest vehicle_imageRequest, MultipartFile image)throws IOException;
    List<Vehicle_imageResponse> getAll();
    Vehicle_imageResponse getById(Long id);
    Vehicle_imageResponse update(Long id,Vehicle_imageRequest vehicle_imageRequest,MultipartFile image)throws IOException;
    void delete(Long id);
}
