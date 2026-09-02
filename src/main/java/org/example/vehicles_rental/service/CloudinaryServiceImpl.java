package org.example.vehicles_rental.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService{
    private final Cloudinary cloudinary;

    @Override
    public String uploadBrandImage(MultipartFile file) throws IOException{
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "vehicle_rental/brands"));

        return result.get("secure_url").toString();
    }
    @Override
    public String uploadVehicleImage(MultipartFile file) throws IOException{
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "vehicle_rental/vehicles"));

        return result.get("secure_url").toString();
    }

    @Override
    public String uploadProfileImage(MultipartFile file) throws IOException{
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "vehicle_rental/profiles"));

        return result.get("secure_url").toString();
    }


}
