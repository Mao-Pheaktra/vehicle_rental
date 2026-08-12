package org.example.vehicles_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.BookingRequest;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.example.vehicles_rental.dto.response.BookingResponse;
import org.example.vehicles_rental.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> create(
            @RequestBody BookingRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>("Booking created successfully",201, bookingService.create(request)));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAll() {
        return ResponseEntity.ok(
                new ApiResponse<>("Bookings retrieved successfully",200, bookingService.getAll()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>("Booking retrieved successfully",200, bookingService.getById(id)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> update(
            @PathVariable Long id,
            @RequestBody BookingRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>("Booking updated successfully", 200,bookingService.update(id, request)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>("Booking deleted successfully",200, null));
    }
}