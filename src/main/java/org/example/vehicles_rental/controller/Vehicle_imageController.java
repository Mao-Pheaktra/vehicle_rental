package org.example.vehicles_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.Vehicle_imageRequest;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.example.vehicles_rental.dto.response.Vehicle_imageResponse;
import org.example.vehicles_rental.service.Vehicle_ImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehicle_image")
public class Vehicle_imageController {
    private final Vehicle_ImageService vehicle_imageService;

    @PostMapping("/create")
    public ApiResponse<Vehicle_imageResponse> create(@ModelAttribute Vehicle_imageRequest  vehicle_imageRequest, @RequestParam("image") MultipartFile image) throws IOException {
        return new ApiResponse<>("create vehicle image successfully",201,vehicle_imageService.create(vehicle_imageRequest,image));
    }
    @GetMapping("/getAll")
    private ApiResponse<List<Vehicle_imageResponse>> getAll(){
        return new ApiResponse<>("get all vehicle image successfully",200,vehicle_imageService.getAll());
    }
    @GetMapping("/getById/{id}")
    private ApiResponse<Vehicle_imageResponse> getById(@PathVariable long id) {
        return new ApiResponse<>("get by id vehicle image successfully",200,vehicle_imageService.getById(id));
    }
    @PutMapping("/update/{id}")
    private ApiResponse<Vehicle_imageResponse> update(@PathVariable Long id,@ModelAttribute Vehicle_imageRequest vehicle_imageRequest,
                                                      @RequestParam("image") MultipartFile image) throws IOException {
        return new ApiResponse<>("update vehicle image successfully",201,vehicle_imageService.update(id,vehicle_imageRequest,image));
    }
    @DeleteMapping("/delete/{id}")
    private String delete(@PathVariable long id) {
        vehicle_imageService.delete(id);
        return "delete vehicle image successfully";
    }
}
