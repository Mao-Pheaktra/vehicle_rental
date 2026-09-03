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
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;



@Service
@RequiredArgsConstructor
public class AuthServiceImple implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final OtpRepository otpRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private final ObjectMapper objectMapper = new ObjectMapper();


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

    @Override
    public LoginResponse googleLogin(String code) {

        try {

            // 1. Exchange Google authorization code for access token
            HttpHeaders tokenHeaders = new HttpHeaders();
            tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
            tokenBody.add("code", code);
            tokenBody.add("client_id", clientId);
            tokenBody.add("client_secret", clientSecret);
            tokenBody.add("redirect_uri", redirectUri);
            tokenBody.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String, String>> tokenRequest =
                    new HttpEntity<>(tokenBody, tokenHeaders);

            ResponseEntity<String> tokenResponse = restTemplate.postForEntity(
                    "https://oauth2.googleapis.com/token",
                    tokenRequest,
                    String.class
            );

            JsonNode tokenJson = objectMapper.readTree(tokenResponse.getBody());

            String accessToken = tokenJson.get("access_token").asText();

            // 2. Get Google user information
            HttpHeaders userHeaders = new HttpHeaders();
            userHeaders.setBearerAuth(accessToken);

            HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

            ResponseEntity<String> userResponse = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET,
                    userRequest,
                    String.class
            );

            JsonNode googleUser = objectMapper.readTree(userResponse.getBody());

            String email = googleUser.get("email").asText();
            String name = googleUser.has("name")
                    ? googleUser.get("name").asText()
                    : email;

            String profileImage = googleUser.has("picture")
                    ? googleUser.get("picture").asText()
                    : null;

            // 3. Find user by email
            Optional<User> existingUser = userRepository.findByEmail(email);

            User user;

            if (existingUser.isPresent()) {

                user = existingUser.get();

                // Update profile image from Google
                if (profileImage != null) {
                    user.setProfileImage(profileImage);
                }

                // Make sure Google account is active
                user.setActive(true);

                userRepository.save(user);

            } else {

                // 4. Create new Google user
                user = User.builder()
                        .name(name)
                        .email(email)
                        .role(Role.CLIENT)
                        .isActive(true)
                        .profileImage(profileImage)
                        .build();

                user = userRepository.save(user);
            }

            // 5. Generate JWT
            String token = jwtService.generateToken(user);

            // 6. Return normal LoginResponse
            return LoginResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .token(token)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Google login failed", e);
        }
    }

}
