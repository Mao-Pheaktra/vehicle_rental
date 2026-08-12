package org.example.vehicles_rental.service;

import org.example.vehicles_rental.dto.request.VehicleRequest;
import org.example.vehicles_rental.dto.response.VehicleResponse;
import org.example.vehicles_rental.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    VehicleResponse create(VehicleRequest  vehicleRequest);
    List<VehicleResponse> getAll();
    VehicleResponse getById(Long id);
    VehicleResponse update(Long id,VehicleRequest vehicleRequest);
    void delete(Long id);
}
