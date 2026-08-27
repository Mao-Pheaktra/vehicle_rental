package org.example.vehicles_rental.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.*;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.example.vehicles_rental.dto.response.LoginResponse;
import org.example.vehicles_rental.dto.response.RegisterResponse;
import org.example.vehicles_rental.dto.response.VerifyOtpResponse;
import org.example.vehicles_rental.exception.TooManyRequestException;
import org.example.vehicles_rental.service.AuthService;
import org.example.vehicles_rental.service.PasswordResetService;
import org.example.vehicles_rental.service.RateLimitService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final RateLimitService rateLimitService;
    private final PasswordResetService passwordResetService;
    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest registerRequest, HttpServletRequest httpServletRequest){
        String ip = httpServletRequest.getRemoteAddr();

        if (!rateLimitService.isAllowed("register:" + ip)){
            throw new TooManyRequestException("Too many registration. Please try again later");
        }
        return new ApiResponse<>("Register successfully",201, authService.register(registerRequest));
    }
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest){
        String ip = httpServletRequest.getRemoteAddr();

        if (!rateLimitService.isAllowed("login:" + ip)){
            throw new TooManyRequestException("Too many login. Please try again later");
        }
        return new ApiResponse<>("Login successfully", 200, authService.login(loginRequest));
    }
    @PostMapping("/verifyOtp")
    public ApiResponse<VerifyOtpResponse> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest){
        return new ApiResponse<>("OTP verified successfully", 200, authService.verifyOtp(verifyOtpRequest));
    }

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        passwordResetService.forgotPassword(forgotPasswordRequest);
        return new ApiResponse<>("Password reset link has beenn sent to your email", 200, null);
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        passwordResetService.resetPassword(resetPasswordRequest);

        return new ApiResponse<>("Password reset successfully", 200, null);
    }

}
