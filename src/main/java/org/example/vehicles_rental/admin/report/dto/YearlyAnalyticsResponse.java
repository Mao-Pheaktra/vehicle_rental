package org.example.vehicles_rental.admin.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YearlyAnalyticsResponse {

    private int year;
    private BigDecimal revenue;
    private long bookings;
}