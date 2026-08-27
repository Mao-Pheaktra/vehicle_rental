package org.example.vehicles_rental.service;

import org.example.vehicles_rental.dto.request.ForgotPasswordRequest;
import org.example.vehicles_rental.dto.request.ResetPasswordRequest;




public interface PasswordResetService {
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    void resetPassword(ResetPasswordRequest resetPasswordRequest);
}
