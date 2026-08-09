package org.example.vehicles_rental.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.vehicles_rental.enums.Role;

@Data
@Builder
public class RegisterResponse {
    private Long id;
    private String name;
    private String email;
    private String pwd;
    private Role role;
}
