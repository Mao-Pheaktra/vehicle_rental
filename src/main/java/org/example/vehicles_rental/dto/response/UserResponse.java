package org.example.vehicles_rental.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.vehicles_rental.enums.Gender;
import org.example.vehicles_rental.enums.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String tell;
    private Gender gender;
    private String profileImage;
    private Role role;
    private Boolean isActive;
}
