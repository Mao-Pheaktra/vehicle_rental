package org.example.vehicles_rental.admin.booking.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.booking.dto.BookingDashboardResponse;
import org.example.vehicles_rental.admin.booking.service.BookingDashboardService;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/bookings")
public class BookingDashboardController {

    private final BookingDashboardService bookingService;

    @GetMapping
    public ResponseEntity<ApiResponse<BookingDashboardResponse>> getBookings() {

        BookingDashboardResponse response =
                bookingService.getBookings();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Bookings retrieved successfully",
                        200,
                        response
                )
        );
    }
}