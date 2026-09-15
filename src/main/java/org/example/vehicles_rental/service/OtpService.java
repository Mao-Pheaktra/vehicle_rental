package org.example.vehicles_rental.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.vehicles_rental.entity.Otp;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {
    private final OtpRepository otpRepository;
    private final EmailService emailService;

    @Value("${app.otp.email-fallback.enabled:true}")
    private boolean emailFallbackEnabled;

    public String generateOtp(){
        return String.format("%06d", new Random().nextInt(1000000));
    }

    @Transactional
    public String createOtp(User user){
        otpRepository.deleteByUserId(user.getId());
        String code = generateOtp();
        Otp otp = Otp.builder()
                .otp(code)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .verified(false)
                .user(user)
                .build();

        otpRepository.saveAndFlush(otp);

        try {
            emailService.sendOtp(user.getEmail(), code);
            return "Registration successful. Please verify the OTP sent to your email.";
        } catch (RuntimeException e) {
            log.warn("Could not send OTP email to {}: {}", user.getEmail(), e.getMessage());

            if (!emailFallbackEnabled) {
                throw e;
            }

            return "Registration successful, but OTP email could not be sent. Use development OTP: " + code;
        }
    }
}
