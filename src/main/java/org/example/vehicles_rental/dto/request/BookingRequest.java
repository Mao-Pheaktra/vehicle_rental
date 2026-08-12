package org.example.vehicles_rental.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private Long userId;
    private Long vehicleId;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private String BStatus;
}