package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.vehicles_rental.dto.request.CreatePaymentRequest;
import org.example.vehicles_rental.dto.response.PaymentResponse;
import org.example.vehicles_rental.entity.Booking;
import org.example.vehicles_rental.entity.Payment;
import org.example.vehicles_rental.entity.PaymentMethod;
import org.example.vehicles_rental.enums.PaymentStatus;
import org.example.vehicles_rental.repository.BookingRepository;
import org.example.vehicles_rental.repository.PaymentMethodRepository;
import org.example.vehicles_rental.repository.PaymentRepository;
import org.example.vehicles_rental.util.KhqrBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BakongPaymentService {

    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Value("${bakong.api.base-url}")
    private String baseUrl;

    @Value("${bakong.api.token}")
    private String token;

    @Value("${bakong.merchant.name}")
    private String merchantName;

    @Value("${bakong.merchant.city}")
    private String merchantCity;

    public PaymentResponse createPayment(CreatePaymentRequest request) {

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found: " + request.getBookingId()));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Payment method not found: " + request.getPaymentMethodId()));

        // 1. Build KHQR string locally
        String billNumber = "BOOK-" + booking.getId();
        String khqrString = KhqrBuilder.build(
                request.getBakongAccount(),
                merchantName,
                merchantCity,
                request.getAmount(),
                request.getCurrency(),
                billNumber,
                null,
                null,
                true // dynamic QR (has fixed amount)
        );

        // 2. MD5 hash of the QR string (used later to check status)
        String md5 = md5Hex(khqrString);

        // 3. Save
        Payment payment = Payment.builder()
                .booking(booking)
                .paymentMethod(paymentMethod)
                .amount(request.getAmount())
                .bakongAccount(request.getBakongAccount())
                .currency(request.getCurrency())
                .qr(khqrString)
                .md5(md5)
                .transactionId(billNumber)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse checkPayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));

        if (payment.getPaymentStatus() == PaymentStatus.PENDING && payment.getMd5() != null) {
            if (isPaidOnBakong(payment.getMd5())) {
                payment.setPaymentStatus(PaymentStatus.PAID);
                payment = paymentRepository.save(payment);
            }
        }

        return toResponse(payment);
    }

    // ---- Bakong Open API: verify transaction ----
    private boolean isPaidOnBakong(String md5) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            Map<String, Object> body = Map.of("md5", md5);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl + "/check_transaction_by_md5",
                    HttpMethod.POST, entity, Map.class);

            Map bodyResp = response.getBody();
            if (bodyResp == null) return false;

            Object responseCode = bodyResp.get("responseCode");
            // Bakong: responseCode = 0 means transaction found & successful
            return responseCode != null && responseCode.toString().equals("0");

        } catch (Exception e) {
            log.warn("Bakong check_transaction_by_md5 failed: {}", e.getMessage());
            return false;
        }
    }

    private String md5Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5 generation failed", e);
        }
    }

    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking().getId())
                .amount(payment.getAmount())
                .paymentMethodId(payment.getPaymentMethod().getId())
                .transactionId(payment.getTransactionId())
                .currency(payment.getCurrency())
                .qr(payment.getQr())
                .md5(payment.getMd5())
                .status(payment.getPaymentStatus())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}