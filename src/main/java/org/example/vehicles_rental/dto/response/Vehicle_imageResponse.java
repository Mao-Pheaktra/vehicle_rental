package org.example.vehicles_rental.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle_imageResponse {
    private Long id;
    private String vehicle_name;
    private String image;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
