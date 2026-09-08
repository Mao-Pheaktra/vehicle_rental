package org.example.vehicles_rental.service;

import jakarta.transaction.Transactional;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;



import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import tools.jackson.databind.ObjectMapper;


@Service
@RequiredArgsConstructor
public class AuthServiceImple implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final OtpRepository otpRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")

    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    private final RestTemplate restTemplate = new RestTemplate();



    @Override

    public RegisterResponse register(RegisterRequest registerRequest) {

        Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (!user.isActive()) {
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
            throw new EmailAlreadyExists("Email already exists");

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
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new EmailAlreadyExists("Incorrect email or password"));
        if (!passwordEncoder.matches(loginRequest.getPwd(), user.getPwd())) {
            throw new EmailAndPasswordNotMatch("Email and password are not match");
        }
        if (!user.isActive()) {
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
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest verifyOtpRequest) {
        User user = userRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Email Not Found"));
        Otp otp = otpRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("OTP Not Found"));
        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otp);
            if (!user.isActive()) {
                userRepository.delete(user);
            }
            throw new OtpExpireException("OTP expired. Please register again");
        }
        if (!otp.getOtp().equals(verifyOtpRequest.getOtp())) {
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

    @Override
    @Transactional
    public LoginResponse googleLogin(String code) {

        // 1. Exchange authorization code for Google access token
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();

        tokenRequest.add("code", code);
        tokenRequest.add("client_id", googleClientId);
        tokenRequest.add("client_secret", googleClientSecret);
        tokenRequest.add("redirect_uri", "http://localhost:8080/api/auth/google/callback");
        tokenRequest.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> tokenEntity = new HttpEntity<>(tokenRequest, tokenHeaders);

        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                tokenEntity,
                Map.class);
        if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenResponse.getBody() == null) {
            throw new FailToGetGgToken("Fail to get Google access token");
        }

        String accessToken = (String) tokenResponse.getBody().get("access_token");
        // 2. Get Google user information
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);

        HttpEntity<Void> userInfoEntity = new HttpEntity<>(userInfoHeaders);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                userInfoEntity,
                Map.class
        );

        if (!userInfoResponse.getStatusCode().is2xxSuccessful()
                || userInfoResponse.getBody() == null) {
            throw new RuntimeException("Failed to get Google user information");
        }

        Map<String, Object> googleUser = userInfoResponse.getBody();


        String email = (String) googleUser.get("email");
        String name = (String) googleUser.get("name");
        String picture = (String) googleUser.get("picture");

        if (email == null || email.isBlank()) {
            throw new RuntimeException("Google account email not found");
        }

        // 3. Find existing user or create a new one
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {

                    User newUser = User.builder()
                            .name(name != null ? name : email)
                            .email(email)
                            .pwd(passwordEncoder.encode(UUID.randomUUID().toString()))
                            .role(Role.CLIENT)
                            .profileImage(picture)
                            .isActive(true)
                            .build();

                    return userRepository.save(newUser);
                });

        // 4. Existing account should also become active
        if (!user.isActive()) {
            user.setActive(true);
            userRepository.save(user);
        }

        // 5. Generate your existing JWT
        String token = jwtService.generateToken(user);

        // 6. Return the same LoginResponse used by normal login
        return LoginResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }
}