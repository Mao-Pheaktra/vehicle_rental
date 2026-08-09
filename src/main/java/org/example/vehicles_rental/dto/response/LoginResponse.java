package org.example.vehicles_rental.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.vehicles_rental.enums.Role;

@Data
@Builder
public class LoginResponse {
    private String token;
    private Long id;
    private String name;
    private String email;
    private Role role;
}

