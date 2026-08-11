package org.example.vehicles_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.UserRequest;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.example.vehicles_rental.dto.response.UserResponse;
import org.example.vehicles_rental.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @PostMapping("/post")
    public ApiResponse<UserResponse> create(@ModelAttribute UserRequest userRequest, @RequestParam("file")MultipartFile file ) throws IOException{
        return new ApiResponse<>("Create user successfully",201, userService.create(userRequest, file));
    }
    @GetMapping("/get")
    public ApiResponse<List<UserResponse>> readAll(){
        return new ApiResponse<>("Get user successfully", 201, userService.readAll());
    }
    @GetMapping("/get/{id}")
    public ApiResponse<UserResponse> readById(@PathVariable Long id){
        return new ApiResponse<>("Get user by id successfully", 201, userService.readById(id));
    }
    @DeleteMapping("/delete/{id}")
    public UserResponse delete(@PathVariable Long id){
        return  userService.delete(id);
    }
    @PutMapping("/update/{id}")
    public UserResponse update(@PathVariable Long id, @ModelAttribute UserRequest userRequest, @RequestParam("file") MultipartFile file) throws IOException{
        return userService.update(id,userRequest,file
        );
    }
}
