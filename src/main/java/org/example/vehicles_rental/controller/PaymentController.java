package org.example.vehicles_rental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.CreatePaymentRequest;
import org.example.vehicles_rental.dto.request.PaymentRequest;
import org.example.vehicles_rental.dto.response.ApiResponse;
import org.example.vehicles_rental.dto.response.BakongPaymentResponse;
import org.example.vehicles_rental.dto.response.PaymentResponse;
import org.example.vehicles_rental.service.BakongPaymentService;
import org.example.vehicles_rental.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final BakongPaymentService bakongPaymentService;
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
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponse> getByBooking(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                paymentService.getByBooking(bookingId)
        );
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
    @PostMapping("/bakong/qr")
    public ResponseEntity<BakongPaymentResponse> createBakongQr(
            @Valid @RequestBody CreatePaymentRequest request) {

        return ResponseEntity.ok(
                bakongPaymentService.createPayment(request)
        );
    }

    @PostMapping("/bakong/test-qr")
    public ResponseEntity<BakongPaymentResponse> createBakongTestQr(
            @Valid @RequestBody CreatePaymentRequest request) {

        return ResponseEntity.ok(
                bakongPaymentService.createTestPayment(request)
        );
    }

    @PostMapping("/bakong/scan-qr")
    public ResponseEntity<BakongPaymentResponse> createBakongScanQr(
            @Valid @RequestBody CreatePaymentRequest request) {

        return ResponseEntity.ok(
                bakongPaymentService.createScanPayment(request)
        );
    }

    @GetMapping("/bakong/status/{bookingId}")
    public ResponseEntity<BakongPaymentResponse> checkBakongPayment(
            @PathVariable Long bookingId,
            @RequestParam(required = false) String reference) {

        return ResponseEntity.ok(
                bakongPaymentService.checkPaymentByBooking(bookingId, reference)
        );
    }

    @GetMapping("/bakong/test-status")
    public ResponseEntity<BakongPaymentResponse> checkBakongTestPayment(
            @RequestParam(required = false) String reference,
            @RequestParam java.math.BigDecimal amount,
            @RequestParam(defaultValue = "USD") String currency) {

        return ResponseEntity.ok(
                bakongPaymentService.checkTestPayment(reference, amount, currency)
        );
    }

    @GetMapping("/bakong/scan-status")
    public ResponseEntity<BakongPaymentResponse> checkBakongScanPayment(
            @RequestParam(required = false) String reference,
            @RequestParam java.math.BigDecimal amount,
            @RequestParam(defaultValue = "USD") String currency) {

        return ResponseEntity.ok(
                bakongPaymentService.checkScanPayment(reference, amount, currency)
        );
    }
}
