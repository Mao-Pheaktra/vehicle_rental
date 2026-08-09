package org.example.vehicles_rental.controller;


import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.LoginRequest;
import org.example.vehicles_rental.dto.request.RegisterRequest;
import org.example.vehicles_rental.dto.request.VerifyOtpRequest;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.example.vehicles_rental.dto.response.LoginResponse;
import org.example.vehicles_rental.dto.response.RegisterResponse;
import org.example.vehicles_rental.dto.response.VerifyOtpResponse;
import org.example.vehicles_rental.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest registerRequest){
        return new ApiResponse<>("Register successfully",201, authService.register(registerRequest));
    }
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return new ApiResponse<>("Login successfully", 201, authService.login(loginRequest));
    }
    @PostMapping("/verifyOtp")
    public ApiResponse<VerifyOtpResponse> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest){
        return new ApiResponse<>("OTP verified successfully", 200, authService.verifyOtp(verifyOtpRequest));
    }
}
