package org.example.vehicles_rental.service;

import org.example.vehicles_rental.dto.request.UserRequest;
import org.example.vehicles_rental.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserResponse create(UserRequest userRequest, MultipartFile file) throws IOException;
    List<UserResponse> readAll();
    UserResponse readById(Long id);
    UserResponse delete(Long id);
    UserResponse update(Long id, UserRequest userRequest, MultipartFile file) throws IOException;

}
