package org.example.vehicles_rental.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String pwd;
}
