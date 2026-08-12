package org.example.vehicles_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.PaymentMethodRequest;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.example.vehicles_rental.dto.response.PaymentMethodResponse;
import org.example.vehicles_rental.service.PaymentMethodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentMethodResponse>> create(
            @RequestBody PaymentMethodRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>("Payment method created successfully", 201,paymentMethodService.create(request)));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentMethodResponse>>> getAll() {
        return ResponseEntity.ok(
                new ApiResponse<>("Payment methods retrieved successfully", 200,paymentMethodService.getAll()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentMethodResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>("Payment method retrieved successfully", 200,paymentMethodService.getById(id)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentMethodResponse>> update(
            @PathVariable Long id,
            @RequestBody PaymentMethodRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>("Payment method updated successfully",200, paymentMethodService.update(id, request)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @PathVariable Long id) {
        paymentMethodService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>("Payment method deleted successfully", 200,null));
    }
}