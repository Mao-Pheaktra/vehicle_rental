package org.example.vehicles_rental.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private Long totalUser;
    private Long totalVehicle;
    private Long availableVehicle;
    private Long activeBooking;

    private BigDecimal monthlyRevenue;

    private List<RevenueResponse> revenueResponses;
    private List<BookingStatisticResponse> bookingStatisticResponses;
    private List<CategoryStatisticResponse> categoryStatisticResponses;
    private List<RecentBookingResponse> recentBookingResponses;
}