package org.example.vehicles_rental.dto.request;

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
public class UserRequest {
    private String name;
    private String email;
    private String tell;
    private Gender gender;
    private String pwd;
    private String profileImage;
}
