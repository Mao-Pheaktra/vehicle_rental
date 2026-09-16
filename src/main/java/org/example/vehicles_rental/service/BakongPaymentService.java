package org.example.vehicles_rental.service;

import kh.gov.nbc.bakong_khqr.BakongKHQR;
import kh.gov.nbc.bakong_khqr.model.IndividualInfo;
import kh.gov.nbc.bakong_khqr.model.KHQRData;
import kh.gov.nbc.bakong_khqr.model.KHQRResponse;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.vehicles_rental.dto.request.CreatePaymentRequest;
import org.example.vehicles_rental.dto.response.BakongPaymentResponse;
import org.example.vehicles_rental.entity.Booking;
import org.example.vehicles_rental.entity.Payment;
import org.example.vehicles_rental.entity.PaymentMethod;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.enums.PaymentMethodName;
import org.example.vehicles_rental.enums.PaymentMethodStatus;
import org.example.vehicles_rental.enums.PaymentStatus;
import org.example.vehicles_rental.enums.Role;
import org.example.vehicles_rental.exception.BookingNotFound;
import org.example.vehicles_rental.exception.NotFoundException;
import org.example.vehicles_rental.exception.PaymentFailed;
import org.example.vehicles_rental.exception.PaymentMethodNotFound;
import org.example.vehicles_rental.exception.PaymentNotFound;
import org.example.vehicles_rental.repository.BookingRepository;
import org.example.vehicles_rental.repository.PaymentMethodRepository;
import org.example.vehicles_rental.repository.PaymentRepository;
import org.example.vehicles_rental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BakongPaymentService {

    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;
    private static final int TEST_PAYMENT_SUCCESS_DELAY_SECONDS = 10;

    @Value("${bakong.api.base-url}")
    private String baseUrl;

    @Value("${bakong.api.token}")
    private String token;

    @Value("${bakong.merchant.account}")
    private String merchantAccount;

    @Value("${bakong.merchant.name}")
    private String merchantName;

    @Value("${bakong.merchant.city}")
    private String merchantCity;

    @Value("${bakong.merchant.acquiring-bank}")
    private String acquiringBank;

    // CREATE BAKONG PAYMENT
    public BakongPaymentResponse createPayment(CreatePaymentRequest request) {

        // VALIDATION
        if (request == null) {
            throw new IllegalArgumentException("Payment request is required");
        }
        if (request.getBookingId() == null) {
            throw new IllegalArgumentException("Booking ID is required");
        }
        if (request.getPaymentMethodId() == null) {
            throw new IllegalArgumentException("Payment method ID is required");
        }
        if (request.getAmount() == null) {
            throw new IllegalArgumentException("Payment amount is required");
        }
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }
        if (request.getCurrency() == null || request.getCurrency().isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }

        String currency = request.getCurrency().trim().toUpperCase();

        if (!currency.equals("USD") && !currency.equals("KHR")) {
            throw new IllegalArgumentException("Currency must be USD or KHR");
        }

        // FIND BOOKING
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new BookingNotFound(request.getBookingId()));

        // FIND PAYMENT METHOD
        PaymentMethod paymentMethod = paymentMethodRepository.findById(
                request.getPaymentMethodId()
        ).orElseThrow(() -> new PaymentMethodNotFound(
                request.getPaymentMethodId()
        ));

        // CHECK PAYMENT METHOD
        if (paymentMethod.getStatus() != PaymentMethodStatus.ACTIVE) {
            throw new IllegalArgumentException("Payment method is currently inactive");
        }

        if (paymentMethod.getPaymentMethodName() != PaymentMethodName.BAKONG) {
            throw new IllegalArgumentException("Payment method must be BAKONG");
        }

        // DUPLICATE PAYMENT CHECK
        if (paymentRepository.existsByBookingId(request.getBookingId())) {
            throw new PaymentAlreadyExists(request.getBookingId());
        }

        // GENERATE BILL NUMBER
        String billNumber = "BOOK-" + booking.getId();

        // CREATE KHQR INFO
        IndividualInfo info = new IndividualInfo();
        info.setBakongAccountId(merchantAccount);
        info.setMerchantName(merchantName);
        info.setMerchantCity(merchantCity);
        info.setAcquiringBank(acquiringBank);
        info.setAmount(amount.doubleValue());
        info.setBillNumber(billNumber);
        info.setExpirationTimestamp(
                java.time.Instant.now().plusSeconds(600).toEpochMilli());

        if ("USD".equals(currency)) {
            info.setCurrency(KHQRCurrency.USD);
        } else {
            info.setCurrency(KHQRCurrency.KHR);
        }

        // GENERATE KHQR
        KHQRResponse<KHQRData> response = BakongKHQR.generateIndividual(info);

        if (response == null) {
            throw new PaymentFailed("KHQR generation failed");
        }

        if (response.getKHQRStatus() == null) {
            throw new PaymentFailed("KHQR status is null");
        }

        if (response.getKHQRStatus().getCode() != 0) {
            throw new PaymentFailed(
                    "KHQR generation failed: " +
                            response.getKHQRStatus().getMessage()
            );
        }

        if (response.getData() == null) {
            throw new PaymentFailed("KHQR data is null");
        }

        return response.getData();
    }

        if (qr == null || qr.isBlank()) {
            throw new PaymentFailed("Generated KHQR is empty");
        }

        if (md5 == null || md5.isBlank()) {
            throw new PaymentFailed("Generated KHQR MD5 is empty");
        }

        // SAVE PAYMENT
        Payment payment = Payment.builder()
                .booking(booking)
                .paymentMethod(paymentMethod)
                .amount(request.getAmount())
                .bakongAccount(merchantAccount)
                .currency(currency)
                .qr(qr)
                .md5(md5)
                .transactionId(billNumber)
                .paymentStatus(PaymentStatus.PENDING)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        if (reference == null || reference.isBlank()) {
            return BakongPaymentResponse.builder()
                    .amount(amount)
                    .currency(normalizedCurrency)
                    .status(PaymentStatus.PENDING)
                    .message("Waiting for QR reference")
                    .build();
        }

        Map<String, Object> result = checkTransactionByMd5(reference);

        if (result == null) {
            return testQrEnabled
                    ? buildTestPaidResponse(reference, amount, normalizedCurrency)
                    : pendingReferenceResponse(null, reference);
        }

        Object responseCode = result.get("responseCode");

        if (!isSuccessfulBakongResponse(responseCode)) {
            return pendingReferenceResponse(
                    null,
                    reference,
                    getString(result, "responseMessage"));
        }

        Object dataObject = result.get("data");

        if (!(dataObject instanceof Map<?, ?> data)) {
            return BakongPaymentResponse.builder()
                    .amount(amount)
                    .currency(normalizedCurrency)
                    .md5(reference)
                    .paidAt(LocalDateTime.now())
                    .paymentDate(LocalDate.now())
                    .status(PaymentStatus.PAID)
                    .build();
        }

        String transactionHash = getString(data, "hash");
        String transactionCurrency = Optional.ofNullable(getString(data, "currency"))
                .orElse(normalizedCurrency);
        BigDecimal transactionAmount = Optional.ofNullable(getAmount(data))
                .orElse(amount);

        log.info(
                "Bakong test payment verified: md5={}, dataKeys={}",
                reference,
                data.keySet());

        return BakongPaymentResponse.builder()
                .amount(transactionAmount)
                .currency(transactionCurrency)
                .transactionId(transactionHash)
                .md5(reference)
                .paidAt(LocalDateTime.now())
                .paymentDate(LocalDate.now())
                .status(PaymentStatus.PAID)
                .build();
    }

    // CHECK BAKONG PAYMENT
    public BakongPaymentResponse checkPayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFound(paymentId));

        // ALREADY PAID
        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            return toResponse(payment);
        }

        // CHECK EXPIRY
        if (payment.getExpiresAt() != null &&
                LocalDateTime.now().isAfter(payment.getExpiresAt())) {

            payment.setPaymentStatus(PaymentStatus.EXPIRED);
            paymentRepository.save(payment);

            payment.getBooking().setStatus(
                    org.example.vehicles_rental.enums.BookingStatus.CONFIRMED);
            bookingRepository.save(payment.getBooking());

            log.info(
                    "Bakong QR expired: paymentId={}, bookingId={}",
                    payment.getId(),
                    payment.getBooking().getId());

            return toResponse(payment);
        }

        // CHECK MD5
        if (payment.getMd5() == null || payment.getMd5().isBlank()) {
            return toResponse(payment);
        }

        // CALL BAKONG API
        Map<String, Object> result = checkTransactionByMd5(payment.getMd5());

        if (result == null) {
            if (testQrEnabled && isReadyForTestPayment(payment)) {
                markPaymentPaid(payment, "TEST-" + payment.getMd5());
            }

            return toResponse(payment);
        }

        Object responseCode = result.get("responseCode");

        if (responseCode == null || !"0".equals(responseCode.toString())) {
            return toResponse(payment);
        }

        // GET TRANSACTION DATA
        Object dataObject = result.get("data");

        if (!(dataObject instanceof Map<?, ?> data)) {
            return toResponse(payment);
        }

        String transactionHash = getString(data, "hash");
        String toAccount = getString(data, "toAccountId");
        String transactionCurrency = getString(data, "currency");
        BigDecimal transactionAmount = getBigDecimal(data, "amount");

        // VERIFY AMOUNT
        boolean amountMatches = transactionAmount != null &&
                payment.getAmount().compareTo(transactionAmount) == 0;

        // VERIFY CURRENCY
        boolean currencyMatches = transactionCurrency != null &&
                payment.getCurrency() != null &&
                payment.getCurrency().equalsIgnoreCase(transactionCurrency);

        // VERIFY DESTINATION
        boolean destinationMatches = toAccount != null &&
                merchantAccount != null &&
                merchantAccount.equalsIgnoreCase(toAccount);

        log.info(
                "Bakong verification: paymentId={}, bookingId={}, " +
                        "amountMatches={}, currencyMatches={}, destinationMatches={}",
                payment.getId(),
                payment.getBooking().getId(),
                amountMatches,
                currencyMatches,
                destinationMatches);

        // PAYMENT SUCCESS
        if (amountMatches && currencyMatches && destinationMatches) {
            payment.setPaymentStatus(PaymentStatus.PAID);

            // paymentDate is set ONLY when payment succeeds.
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaidAt(LocalDateTime.now());
            payment.setBakongTransactionHash(transactionHash);

            paymentRepository.save(payment);

            log.info(
                    "Payment verified successfully: paymentId={}, bookingId={}, hash={}",
                    payment.getId(),
                    payment.getBooking().getId(),
                    transactionHash);
        }

        return toResponse(payment);
    }

    // CHECK TRANSACTION BY MD5
    private Map<String, Object> checkTransactionByMd5(String md5) {

        String apiToken = normalizeApiToken(token);

        if (apiToken == null || apiToken.isBlank()) {
            return Map.of(
                    "responseCode", "CONFIG_ERROR",
                    "responseMessage", "Bakong API token is missing. Set BAKONG_API_TOKEN and restart backend.");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = Map.of("md5", md5);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    normalizeBaseUrl(baseUrl) + "/v1/check_transaction_by_md5",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    });

            return response.getBody();

        } catch (RestClientResponseException e) {

            String message = e.getStatusCode().value() == 401
                    ? "Bakong payment verification is unavailable. Please check Bakong API token."
                    : "Bakong API error: " + e.getStatusCode().value();

            log.debug("Bakong API error: {}", message);

            return Map.of(
                    "responseCode", "API_ERROR",
                    "responseMessage", message);

        } catch (Exception e) {
            log.warn("Bakong API error: {}", e.getMessage());
            return null;
        }

        String normalized = value.trim();

        if (normalized.regionMatches(true, 0, "Bearer ", 0, 7)) {
            normalized = normalized.substring(7).trim();
        }

        return normalized.replace("\\_", "_");
    }

    private String normalizeBaseUrl(String value) {

        String normalized = value == null || value.isBlank()
                ? "https://api-bakong.nbc.gov.kh"
                : value.trim();

        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        if (normalized.endsWith("/v1")) {
            normalized = normalized.substring(0, normalized.length() - 3);
        }

        return normalized;
    }

    private boolean isSuccessfulBakongResponse(Object responseCode) {

        if (responseCode == null) {
            return false;
        }

        String code = responseCode.toString().trim();

        return "0".equals(code)
                || "00".equals(code)
                || "success".equalsIgnoreCase(code)
                || "successful".equalsIgnoreCase(code);
    }

    // GET STRING
    private String getString(Map<?, ?> map, String key) {
        Object value = map.get(key);
        return value == null ? null : value.toString();
    }

    // GET BIG DECIMAL
    private BigDecimal getBigDecimal(Map<?, ?> map, String key) {
        Object value = map.get(key);

        if (value == null) {
            return null;
        }

        try {
            return new BigDecimal(value.toString());

        } catch (NumberFormatException e) {
            return null;
        }
    }
    // RESPONSE
    private BakongPaymentResponse toResponse(Payment payment) {
        return BakongPaymentResponse.builder()
                .paymentId(payment.getId())
                .bookingId(payment.getBooking().getId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .transactionId(payment.getTransactionId())
                .qr(payment.getQr())
                .md5(payment.getMd5())
                .expiresAt(payment.getExpiresAt())
                .paidAt(payment.getPaidAt())
                .status(payment.getPaymentStatus())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}
