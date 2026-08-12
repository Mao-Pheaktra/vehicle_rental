package org.example.vehicles_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.PaymentRequest;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.example.vehicles_rental.dto.response.PaymentResponse;
import org.example.vehicles_rental.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> create(
            @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>("Payment created successfully", 201,paymentService.create(request)));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAll() {
        return ResponseEntity.ok(
                new ApiResponse<>("Payments retrieved successfully",200, paymentService.getAll()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>("Payment retrieved successfully",200, paymentService.getById(id)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> update(
            @PathVariable Long id,
            @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>("Payment updated successfully", 200,paymentService.update(id, request)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>("Payment deleted successfully",200, null));
    }
}