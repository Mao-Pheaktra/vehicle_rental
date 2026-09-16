package org.example.vehicles_rental.service;

import org.example.vehicles_rental.dto.request.VehicleRequest;
import org.example.vehicles_rental.dto.response.VehicleResponse;
import org.example.vehicles_rental.entity.Vehicle;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VehicleService {
    VehicleResponse create(VehicleRequest  vehicleRequest, MultipartFile mainImage)throws IOException;
    List<VehicleResponse> getAll();
    VehicleResponse getById(Long id);
    VehicleResponse update(Long id,VehicleRequest vehicleRequest, MultipartFile mainImage) throws IOException;
    void delete(Long id);
}
