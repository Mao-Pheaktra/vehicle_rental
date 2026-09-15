package org.example.vehicles_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.VehicleRequest;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.example.vehicles_rental.dto.response.VehicleResponse;
import org.example.vehicles_rental.service.VehicleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehicle")
public class VehicleController {
    private final VehicleService vehicleService;
    @PostMapping("/create")
    public ApiResponse<VehicleResponse> create(@RequestBody VehicleRequest vehicleRequest, @RequestParam ("mainImage")MultipartFile mainImage) throws IOException {
        return new ApiResponse<>("create vehicle successfully",201, vehicleService.create(vehicleRequest,mainImage));
    }
    @GetMapping("/getAll")
    public ApiResponse<List<VehicleResponse>> getAll(){
        return new ApiResponse<>("get all vehicles successfully",200, vehicleService.getAll());
    }
    @GetMapping("/getById/{id}")
    public ApiResponse<VehicleResponse> getById(@PathVariable Long id){
        return new ApiResponse<>("get vehicle ById successfully",200, vehicleService.getById(id));
    }
    @PutMapping("/update/{id}")
    public ApiResponse<VehicleResponse> update(@PathVariable Long id,@RequestBody VehicleRequest vehicleRequest, @RequestParam ("mainImage")MultipartFile mainImage) throws IOException {
        return new ApiResponse<>("update vehicle successfully",201, vehicleService.update(id, vehicleRequest, mainImage));
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        vehicleService.delete(id);
        return "delete vehicle successfully";
    }
}
