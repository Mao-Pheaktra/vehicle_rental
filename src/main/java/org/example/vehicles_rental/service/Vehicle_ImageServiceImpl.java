package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.Vehicle_imageRequest;
import org.example.vehicles_rental.dto.response.VehicleResponse;
import org.example.vehicles_rental.dto.response.Vehicle_imageResponse;
import org.example.vehicles_rental.entity.Vehicle;
import org.example.vehicles_rental.entity.Vehicle_Image;
import org.example.vehicles_rental.exception.NotFoundException;
import org.example.vehicles_rental.mapper.Vehicle_ImageMapper;
import org.example.vehicles_rental.repository.VehicleRepository;
import org.example.vehicles_rental.repository.Vehicle_imageRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Vehicle_ImageServiceImpl implements Vehicle_ImageService {
    private final Vehicle_imageRepository vehicle_imageRepository;
    private final Vehicle_ImageMapper  vehicle_ImageMapper;
    private final VehicleRepository  vehicleRepository;

    @Override
    public Vehicle_imageResponse create(Vehicle_imageRequest vehicle_imageRequest, MultipartFile image)throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        String imageName = image.getOriginalFilename();
        String imageUrl = UUID.randomUUID().toString()+"_"+image.getOriginalFilename();
        Path path = Paths.get("upload");
        String fileUrl = "http://localhost:8080/upload/"+imageUrl;
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        Files.copy(image.getInputStream(),path.resolve(imageUrl));

        Vehicle vehicle = vehicleRepository
                .findById(vehicle_imageRequest.getVehicle_id())
                .orElseThrow(()->new RuntimeException("vehicle id not found."));
        Vehicle_Image vehicle_image = Vehicle_Image.builder()
                .vehicle(vehicle)
                .image(imageUrl)
                .build();
        vehicle_imageRepository.save(vehicle_image);
        return vehicle_ImageMapper.toVehicleImageResponse(vehicle_image);
    }

    @Override
    public List<Vehicle_imageResponse> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<Vehicle_Image>  vehicle_images = vehicle_imageRepository.findAll();
        List<Vehicle_imageResponse>  vehicle_imageResponses = new ArrayList<>();
        for (Vehicle_Image vehicle_image : vehicle_images) {
            Vehicle_imageResponse vehicleImageResponse = vehicle_ImageMapper.toVehicleImageResponse(vehicle_image);
            vehicle_imageResponses.add(vehicleImageResponse);
        }
        return vehicle_imageResponses;
    }

    @Override
    public Vehicle_imageResponse getById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Vehicle_Image vehicle_image = vehicle_imageRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Vehicle_Image Id " +id+ " Not Found"));
        return vehicle_ImageMapper.toVehicleImageResponse(vehicle_image);
    }

    @Override
    public Vehicle_imageResponse update(Long id, Vehicle_imageRequest vehicle_imageRequest, MultipartFile image) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        String imageName = image.getOriginalFilename();
        String imageUrl = UUID.randomUUID().toString()+"_"+image.getOriginalFilename();
        Path path = Paths.get("upload");
        String fileUrl = "http://localhost:8080/upload/"+imageUrl;
        Files.copy(image.getInputStream(),path.resolve(imageUrl));

        Vehicle vehicle = vehicleRepository
                .findById(vehicle_imageRequest.getVehicle_id())
                .orElseThrow(()->new RuntimeException("vehicle id not found."));
        Vehicle_Image vehicle_image = vehicle_imageRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Vehicle_Image Id " +id+ " Not Found"));
        vehicle_image.setVehicle(vehicle);
        vehicle_image.setImage(imageUrl);
        vehicle_imageRepository.save(vehicle_image);
        return vehicle_ImageMapper.toVehicleImageResponse(vehicle_image);
    }

    @Override
    public void delete(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Vehicle_Image vehicle_image = vehicle_imageRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Vehicle_Image Id " +id+ " Not Found"));
        vehicle_imageRepository.delete(vehicle_image);
    }
}
