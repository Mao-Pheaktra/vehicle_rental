package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.VehicleRequest;
import org.example.vehicles_rental.dto.response.VehicleResponse;
import org.example.vehicles_rental.entity.Brand;
import org.example.vehicles_rental.entity.Categories;
import org.example.vehicles_rental.entity.Vehicle;
import org.example.vehicles_rental.exception.NotFoundException;
import org.example.vehicles_rental.mapper.VehicleMapper;
import org.example.vehicles_rental.repository.BrandRepository;
import org.example.vehicles_rental.repository.CategoryRepository;
import org.example.vehicles_rental.repository.VehicleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public VehicleResponse create(VehicleRequest vehicleRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Categories categories = categoryRepository
                .findById(vehicleRequest.getCategory_id())
                .orElseThrow(()-> new RuntimeException("Category Not Found"));
        Brand brand = brandRepository
                .findById(vehicleRequest.getBrand_id())
                .orElseThrow(()-> new RuntimeException("Brand Not Found"));
        Vehicle vehicle = Vehicle.builder()
                .category(categories)
                .brand(brand)
                .name(vehicleRequest.getName())
                .description(vehicleRequest.getDescription())
                .model(vehicleRequest.getModel())
                .year(vehicleRequest.getYear())
                .seat(vehicleRequest.getSeat())
                .status(vehicleRequest.getStatus())
                .fuel_type(vehicleRequest.getFuel_type())
                .price_per_day(vehicleRequest.getPrice_per_day())
                .plate_number(vehicleRequest.getPlate_number())
                .transmission(vehicleRequest.getTransmission())
                .build();
        vehicleRepository.save(vehicle);
        return vehicleMapper.toVehicleResponse(vehicle);
    }

    @Override
    public List<VehicleResponse> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
            List<Vehicle> vehicles = vehicleRepository.findAll();
            List<VehicleResponse> vehicleResponses = new ArrayList<>();
            for (Vehicle vehicle1 : vehicles) {
                vehicleResponses.add(vehicleMapper.toVehicleResponse(vehicle1));
            }
        return vehicleResponses;
    }

    @Override
    public VehicleResponse getById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(()->new NotFoundException("vehicle id " +id+ "not found."));
        return vehicleMapper.toVehicleResponse(vehicle);
    }

    @Override
    public VehicleResponse update(Long id, VehicleRequest vehicleRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(()->new NotFoundException("vehicle id " +id+ "not found."));
        Categories categories = categoryRepository
                .findById(vehicleRequest.getCategory_id())
                .orElseThrow(()-> new RuntimeException("Category Not Found"));
        Brand brand = brandRepository
                .findById(vehicleRequest.getBrand_id())
                .orElseThrow(()-> new RuntimeException("Brand Not Found"));
                vehicle.setCategory(categories);
                vehicle.setBrand(brand);
                vehicle.setName(vehicleRequest.getName());
                vehicle.setDescription(vehicleRequest.getDescription());
                vehicle.setModel(vehicleRequest.getModel());
                vehicle.setYear(vehicleRequest.getYear());
                vehicle.setSeat(vehicleRequest.getSeat());
                vehicle.setStatus(vehicleRequest.getStatus());
                vehicle.setFuel_type(vehicleRequest.getFuel_type());
                vehicle.setPlate_number(vehicleRequest.getPlate_number());
                vehicle.setPrice_per_day(vehicleRequest.getPrice_per_day());
                vehicle.setTransmission(vehicleRequest.getTransmission());
                vehicle =vehicleRepository.save(vehicle);

        return vehicleMapper.toVehicleResponse(vehicle);
    }

    @Override
    public void delete(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Vehicle vehicle = vehicleRepository.findById(id)
                        .orElseThrow(()-> new NotFoundException("vehicle id " +id+ "not found."));
        vehicleRepository.deleteById(id);
    }
}
