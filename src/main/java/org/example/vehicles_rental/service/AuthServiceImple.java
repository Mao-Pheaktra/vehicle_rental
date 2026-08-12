package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.LoginRequest;
import org.example.vehicles_rental.dto.request.RegisterRequest;
import org.example.vehicles_rental.dto.request.VerifyOtpRequest;
import org.example.vehicles_rental.dto.response.LoginResponse;
import org.example.vehicles_rental.dto.response.RegisterResponse;
import org.example.vehicles_rental.dto.response.VerifyOtpResponse;
import org.example.vehicles_rental.entity.Otp;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.enums.Role;
import org.example.vehicles_rental.exception.*;
import org.example.vehicles_rental.repository.OtpRepository;
import org.example.vehicles_rental.repository.UserRepository;
import org.example.vehicles_rental.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImple implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final OtpRepository otpRepository;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest){
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new EmaliAlreadyExists("Email already exists");
        }
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .pwd(passwordEncoder.encode(registerRequest.getPwd()))
                .role(Role.CLIENT)
                .isActive(false)
                .build();
        user = userRepository.save(user);
        otpService.createOtp(user);
        RegisterResponse registerResponse = RegisterResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
        return RegisterResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
    @Override
    public LoginResponse login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new EmaliAlreadyExists("Incorrect email or password"));
        if (!passwordEncoder.matches(loginRequest.getPwd(), user.getPwd())){
            throw new EmailAndPasswordNotMatch("Email and password are not match");
        }
        if (!user.isActive()){
            throw new EmailVerify("Please verify your email first");
        }
        String token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }
    @Override
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest verifyOtpRequest) {
        User user = userRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Email Not Found"));
        Otp otp = otpRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("OTP Not Found"));
        if (!otp.getOtp().equals(verifyOtpRequest.getOtp())) {
            throw new InvalidOTP("Incorrect OTP");
        }

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new InvalidOTP("OTP has expired");
        }
        otp.setVerified(true);

        user.setActive(true);
        userRepository.save(user);

        otpRepository.delete(otp);



        return VerifyOtpResponse.builder()
                .message("Email verified successfully")
                .build();
    }
}
