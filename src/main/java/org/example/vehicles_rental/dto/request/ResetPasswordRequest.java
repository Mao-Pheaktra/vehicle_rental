package org.example.vehicles_rental.dto.request;


import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String password;
    private String confirmPassword;

}
