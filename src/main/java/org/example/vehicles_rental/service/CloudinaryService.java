package org.example.vehicles_rental.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface CloudinaryService {
    String uploadBrandImage(MultipartFile file) throws IOException;
    String uploadVehicleImage(MultipartFile file) throws IOException;
    String uploadProfileImage(MultipartFile file) throws IOException;

}
