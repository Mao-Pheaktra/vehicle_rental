package org.example.vehicles_rental.admin.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.vehicles_rental.entity.Booking;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDashboardResponse {
    private Long totalBookings;

    private Long pendingBookings;

    private Long confirmedBookings;

    private Long activeBookings;

    private Long completedBookings;

    private Long cancelledBookings;

    private BigDecimal bookingGrowthPercentage;

    private BigDecimal cancelledRate;

    private List<Booking> bookings;
}
