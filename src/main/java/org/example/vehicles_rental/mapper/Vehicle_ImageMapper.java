package org.example.vehicles_rental.mapper;

import org.example.vehicles_rental.dto.response.VehicleResponse;
import org.example.vehicles_rental.dto.response.Vehicle_imageResponse;
import org.example.vehicles_rental.entity.Vehicle;
import org.example.vehicles_rental.entity.Vehicle_Image;
import org.springframework.stereotype.Component;

@Component
public class Vehicle_ImageMapper {
    public Vehicle_imageResponse toVehicleImageResponse(Vehicle_Image vehicle_image) {
        return Vehicle_imageResponse.builder()
                .id(vehicle_image.getId())
                .vehicle_name(vehicle_image.getVehicle().getName())
                .image(vehicle_image.getImage())
                .created_at(vehicle_image.getCreated_at())
                .updated_at(vehicle_image.getUpdated_at())
                .build();
    }
}
