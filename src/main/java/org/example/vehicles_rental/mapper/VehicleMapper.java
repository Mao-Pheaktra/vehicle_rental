package org.example.vehicles_rental.mapper;

import org.example.vehicles_rental.dto.response.VehicleResponse;
import org.example.vehicles_rental.entity.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {
    public VehicleResponse toVehicleResponse(Vehicle vehicle) {
       return VehicleResponse.builder()
                .id(vehicle.getId())
                .category_name(vehicle.getCategory().getCategory_name())
                .brand_name(vehicle.getBrand().getBrand_name())
                .name(vehicle.getName())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .plate_number(vehicle.getPlate_number())
                .transmission(vehicle.getTransmission())
                .fuel_type(vehicle.getFuel_type())
                .seat(vehicle.getSeat())
                .description(vehicle.getDescription())
                .pricePerDay(vehicle.getPricePerDay())
                .status(vehicle.getStatus())
                .create_at(vehicle.getCreated_at())
                .update_at(vehicle.getUpdated_at())
                .build();
    }
}
