package org.example.vehicles_rental.service;


import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.ForgotPasswordRequest;
import org.example.vehicles_rental.dto.request.ResetPasswordRequest;
import org.example.vehicles_rental.entity.PasswordResetToken;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.exception.EmailAndPasswordNotMatch;
import org.example.vehicles_rental.exception.InvalidResetToken;
import org.example.vehicles_rental.exception.NotFoundException;
import org.example.vehicles_rental.exception.ResetTokenExpired;
import org.example.vehicles_rental.repository.PasswordResetTokenRepository;
import org.example.vehicles_rental.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PasswordResetServiceImpl implements PasswordResetService{

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest){
        String email = forgotPasswordRequest.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("Email not found"));

        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        passwordResetTokenRepository.save(resetToken);

        String resetLink = "http://localhost:5173/reset-password?token=" +token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset Your Password");
        message.setText(
                "Hello,\n\n"
                        + "You requested to reset your password.\n\n"
                        + "Click the link below to create a new password:\n\n"
                        + resetLink
                        + "\n\n"
                        + "This link will expire in 30 minutes.\n\n"
                        + "If you did not request this, please ignore this email."
        );
        mailSender.send(message);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest){
        String token = resetPasswordRequest.getToken();;
        String password = resetPasswordRequest.getPassword();
        String confirmPassword = resetPasswordRequest.getConfirmPassword();

        if(!password.equals(confirmPassword)){
            throw new EmailAndPasswordNotMatch("Email and password not match");
        }

        PasswordResetToken resetToken  = passwordResetTokenRepository.findByToken(token).orElseThrow(()-> new InvalidResetToken("Invalid reset token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            passwordResetTokenRepository.delete(resetToken);
            throw new ResetTokenExpired("Reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPwd(passwordEncoder.encode(password));
        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
    }
}
