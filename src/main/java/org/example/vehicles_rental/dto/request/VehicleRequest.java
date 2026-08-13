package org.example.vehicles_rental.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.vehicles_rental.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequest {
    private Long category_id;
    private Long brand_id;
    private String name;
    private String model;
    private Integer year;
    private String plate_number;
    private String transmission;
    private String fuel_type;
    private Integer seat;
    private BigDecimal pricePerDay;
    private Status status;
    private String description;
    private LocalDateTime create_at;
    private LocalDateTime update_at;

}
