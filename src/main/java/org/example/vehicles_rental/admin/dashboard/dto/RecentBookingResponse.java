package org.example.vehicles_rental.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecentBookingResponse {

    private Long bookingId;
    private String customerName;
    private String vehicleName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private BigDecimal totalAmount;
}