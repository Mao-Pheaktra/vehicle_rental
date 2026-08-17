package org.example.vehicles_rental.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.dto.request.ChangePasswordRequest;
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
import java.util.Optional;

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

        Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser.isPresent()){
            User user = existingUser.get();

            if (!user.isActive()){
                user.setName(registerRequest.getName());
                user.setPwd(passwordEncoder.encode(registerRequest.getPwd()));

                userRepository.save(user);

                otpService.createOtp(user);
                return RegisterResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .pwd(user.getPwd())
                        .role(user.getRole())
                        .message("Registration successful. Please verify the OTP sent to your email.")
                        .build();
            }
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

        return RegisterResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .message("Registration successful. Please verify the OTP sent to your email.")
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
    @Transactional
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest verifyOtpRequest){
        User user = userRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(()-> new NotFoundException("Email Not Found"));
        Otp otp = otpRepository.findByUser(user)
                .orElseThrow(()-> new NotFoundException("OTP Not Found"));
        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otp);
            if (!user.isActive()) {
                userRepository.delete(user);
            }
            throw new OtpExpireException("OTP expired. Please register again");
        }
        if (!otp.getOtp().equals(verifyOtpRequest.getOtp())){
            throw new InvalidOTP("Invalid OTP");
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
