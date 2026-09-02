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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final RateLimitService rateLimitService;
    private final PasswordResetService passwordResetService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
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

    @GetMapping("/google")
    public ApiResponse<String> googleLogin() {

        String googleUrl =
                "https://accounts.google.com/o/oauth2/v2/auth" +
                        "?client_id=" + clientId +
                        "&redirect_uri=" + redirectUri +
                        "&response_type=code" +
                        "&scope=openid%20profile%20email" +
                        "&access_type=offline";

        return new ApiResponse<>(
                "Google login URL generated successfully",
                200,
                googleUrl
        );
    }

    @GetMapping("/google/callback")
    public ApiResponse<LoginResponse> googleCallback(
            @RequestParam("code") String code
    ) {

        return new ApiResponse<>(
                "Google login successfully",
                200,
                authService.googleLogin(code)
        );
    }

}
