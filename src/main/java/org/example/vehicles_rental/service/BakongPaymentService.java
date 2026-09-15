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
import java.time.LocalDate;
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

    @Value("${bakong.test-qr.enabled:false}")
    private boolean testQrEnabled;

    public BakongPaymentResponse createTestPayment(CreatePaymentRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Payment request is required");
        }

        if (request.getAmount() == null) {
            throw new IllegalArgumentException("Payment amount is required");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        String currency = normalizeCurrency(request.getCurrency());
        String billNumber = "TEST-" + System.currentTimeMillis();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10);
        KHQRData khqrData = generateKhqrData(
                request.getAmount(),
                currency,
                billNumber);

        return BakongPaymentResponse.builder()
                .amount(request.getAmount())
                .currency(currency)
                .transactionId(billNumber)
                .qr(khqrData.getQr())
                .md5(khqrData.getMd5())
                .expiresAt(expiresAt)
                .status(PaymentStatus.PENDING)
                .build();
    }

    public BakongPaymentResponse createScanPayment(CreatePaymentRequest request) {

        validateScanPaymentRequest(request);

        String currency = normalizeCurrency(request.getCurrency());
        String billNumber = Optional.ofNullable(request.getBookingId())
                .map(id -> "BOOK-" + id)
                .orElseGet(() -> "PAY-" + System.currentTimeMillis());
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10);
        KHQRData khqrData = generateKhqrData(
                request.getAmount(),
                currency,
                billNumber);

        return BakongPaymentResponse.builder()
                .bookingId(request.getBookingId())
                .amount(request.getAmount())
                .currency(currency)
                .transactionId(billNumber)
                .qr(khqrData.getQr())
                .md5(khqrData.getMd5())
                .expiresAt(expiresAt)
                .status(PaymentStatus.PENDING)
                .build();
    }

    public BakongPaymentResponse createPayment(CreatePaymentRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Payment request is required");
        }

        if (request.getBookingId() == null) {
            throw new IllegalArgumentException("Booking ID is required");
        }

        if (request.getAmount() == null) {
            throw new IllegalArgumentException("Payment amount is required");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        String currency = normalizeCurrency(request.getCurrency());

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new BookingNotFound(request.getBookingId()));

        try {
            assertCanAccessBooking(booking);
        } catch (AccessDeniedException e) {
            log.warn(
                    "Booking payment access denied for bookingId={}; generating scan KHQR instead",
                    request.getBookingId());
            return createScanPayment(request);
        }
        validateBookingAmount(booking, request.getAmount());

        PaymentMethod paymentMethod = paymentMethodRepository
                .findByPaymentMethodNameAndStatus(
                        PaymentMethodName.BAKONG,
                        PaymentMethodStatus.ACTIVE)
                .orElseThrow(() -> new PaymentMethodNotFound("Active BAKONG payment method not found"));

        String billNumber = "BOOK-" + booking.getId();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10);

        Optional<Payment> existingPayment = paymentRepository
                .findFirstByBookingIdOrderByCreatedAtDesc(request.getBookingId());

        if (existingPayment.isPresent()) {
            Payment payment = existingPayment.get();

            if (payment.getPaymentStatus() == PaymentStatus.PENDING
                    && payment.getExpiresAt() != null
                    && LocalDateTime.now().isBefore(payment.getExpiresAt())) {
                return toResponse(payment);
            }
        }

        KHQRData khqrData = generateKhqrData(
                booking.getTotalPrice(),
                currency,
                billNumber);
        String qr = khqrData.getQr();
        String md5 = khqrData.getMd5();

        if (qr == null || qr.isBlank()) {
            throw new PaymentFailed("Generated KHQR is empty");
        }

        if (md5 == null || md5.isBlank()) {
            throw new PaymentFailed("Generated KHQR MD5 is empty");
        }

        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(booking.getTotalPrice());
        payment.setBakongAccount(merchantAccount);
        payment.setCurrency(currency);
        payment.setQr(qr);
        payment.setMd5(md5);
        payment.setTransactionId(billNumber);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setExpiresAt(expiresAt);
        payment.setPaidAt(null);
        payment.setBakongTransactionHash(null);

        Payment savedPayment = paymentRepository.save(payment);

        log.info(
                "Bakong payment created: paymentId={}, bookingId={}, md5={}",
                savedPayment.getId(),
                booking.getId(),
                md5);

        return toResponse(savedPayment);
    }

    private String normalizeCurrency(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }

        String currency = value.trim().toUpperCase();

        if (!currency.equals("USD") && !currency.equals("KHR")) {
            throw new IllegalArgumentException("Currency must be USD or KHR");
        }

        return currency;
    }

    private void validateScanPaymentRequest(CreatePaymentRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Payment request is required");
        }

        if (request.getAmount() == null) {
            throw new IllegalArgumentException("Payment amount is required");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }
    }

    private KHQRData generateKhqrData(
            BigDecimal amount,
            String currency,
            String billNumber) {

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

        KHQRResponse<KHQRData> response = BakongKHQR.generateIndividual(info);

        if (response.getKHQRStatus() == null) {
            throw new PaymentFailed("KHQR status is null");
        }

        if (response.getKHQRStatus().getCode() != 0) {
            throw new PaymentFailed(
                    "KHQR generation failed: "
                            + response.getKHQRStatus().getMessage());
        }

        if (response.getData() == null) {
            throw new PaymentFailed("KHQR data is null");
        }

        return response.getData();
    }

    public BakongPaymentResponse checkPayment(Long paymentId) {

        if (paymentId == null) {
            throw new IllegalArgumentException("Payment ID is required");
        }

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFound(paymentId));

        return verifyPayment(payment);
    }

    public BakongPaymentResponse checkTestPayment(
            String reference,
            BigDecimal amount,
            String currency) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        String normalizedCurrency = normalizeCurrency(currency);

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

    public BakongPaymentResponse checkScanPayment(
            String reference,
            BigDecimal amount,
            String currency) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        String normalizedCurrency = normalizeCurrency(currency);

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
            return pendingReferenceResponse(null, reference);
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

    public BakongPaymentResponse checkPaymentByBooking(Long bookingId) {
        return checkPaymentByBooking(bookingId, null);
    }

    public BakongPaymentResponse checkPaymentByBooking(
            Long bookingId,
            String reference) {

        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID is required");
        }

        if (reference != null && !reference.isBlank()) {
            Optional<Payment> referencePayment = paymentRepository.findByMd5(reference);

            if (referencePayment.isPresent()) {
                Payment payment = referencePayment.get();

                if (!payment.getBooking().getId().equals(bookingId)) {
                    throw new AccessDeniedException("Payment reference does not belong to this booking");
                }

                assertCanAccessBooking(payment.getBooking());
                return verifyPayment(payment);
            }

            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new BookingNotFound(bookingId));

            assertCanAccessBooking(booking);

            return verifyReferencePayment(booking, reference);
        }

        Optional<Payment> payment = paymentRepository
                .findFirstByBookingIdOrderByCreatedAtDesc(bookingId);

        if (payment.isPresent()) {
            assertCanAccessBooking(payment.get().getBooking());
            return verifyPayment(payment.get());
        }

        throw new PaymentNotFound("Payment not found for booking: " + bookingId);
    }

    private BakongPaymentResponse verifyPayment(Payment payment) {

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            return toResponse(payment);
        }

        if (payment.getExpiresAt() != null
                && LocalDateTime.now().isAfter(payment.getExpiresAt())) {

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

        if (payment.getMd5() == null || payment.getMd5().isBlank()) {
            return toResponse(payment);
        }

        Map<String, Object> result = checkTransactionByMd5(payment.getMd5());

        if (result == null) {
            if (testQrEnabled && isReadyForTestPayment(payment)) {
                markPaymentPaid(payment, "TEST-" + payment.getMd5());
            }

            return toResponse(payment);
        }

        Object responseCode = result.get("responseCode");

        if (!isSuccessfulBakongResponse(responseCode)) {

            String responseMessage = getString(result, "responseMessage");
            if (testQrEnabled
                    && isBakongVerificationUnavailable(result)
                    && isReadyForTestPayment(payment)) {
                markPaymentPaid(payment, "TEST-" + payment.getMd5());
                return toResponse(payment);
            }

            log.debug(
                    "Bakong transaction is not available yet: md5={}, responseCode={}, responseMessage={}",
                    payment.getMd5(),
                    responseCode,
                    responseMessage);
            return toResponse(payment);
        }

        Object dataObject = result.get("data");

        if (!(dataObject instanceof Map<?, ?> data)) {
            return toResponse(payment);
        }

        String transactionHash = firstString(
                data,
                "hash",
                "transactionHash",
                "txHash",
                "transactionId");
        String toAccount = firstString(
                data,
                "toAccountId",
                "toBakongAccountId",
                "receiverAccountId",
                "receiverBakongAccountId",
                "toAccount",
                "receiverAccount",
                "receiver",
                "receiverId",
                "merchantAccount");
        String transactionCurrency = firstString(
                data,
                "currency",
                "transactionCurrency");
        BigDecimal transactionAmount = firstAmount(
                data,
                "amount",
                "transactionAmount",
                "paymentAmount",
                "totalAmount");

        boolean amountMatches = transactionAmount == null
                || payment.getAmount().compareTo(transactionAmount) == 0;

        boolean currencyMatches = transactionCurrency == null
                || transactionCurrency.equalsIgnoreCase(payment.getCurrency());

        boolean destinationMatches = toAccount == null
                || merchantAccount == null
                || toAccount.equalsIgnoreCase(merchantAccount);

        log.info(
                "Bakong verification: paymentId={}, bookingId={}, " +
                        "amountMatches={}, currencyMatches={}, destinationMatches={}",
                payment.getId(),
                payment.getBooking().getId(),
                amountMatches,
                currencyMatches,
                destinationMatches);

        if (amountMatches
                && currencyMatches
                && destinationMatches) {

            markPaymentPaid(payment, transactionHash);

            log.info(
                    "Payment verified successfully: paymentId={}, bookingId={}, hash={}",
                    payment.getId(),
                    payment.getBooking().getId(),
                    transactionHash);
        }

        return toResponse(payment);
    }

    private BakongPaymentResponse verifyReferencePayment(
            Booking booking,
            String reference) {

        Map<String, Object> result = checkTransactionByMd5(reference);

        if (result == null) {
            return pendingReferenceResponse(booking.getId(), reference);
        }

        Object responseCode = result.get("responseCode");

        if (!isSuccessfulBakongResponse(responseCode)) {
            String responseMessage = getString(result, "responseMessage");
            log.info(
                    "Bakong reference pending: bookingId={}, md5={}, responseCode={}, responseMessage={}",
                    booking.getId(),
                    reference,
                    responseCode,
                    responseMessage);
            return pendingReferenceResponse(booking.getId(), reference, responseMessage);
        }

        Object dataObject = result.get("data");

        if (!(dataObject instanceof Map<?, ?> data)) {
            log.info(
                    "Bakong reference pending: bookingId={}, md5={}, responseCode={}, hasData=false",
                    booking.getId(),
                    reference,
                    responseCode);
            return pendingReferenceResponse(booking.getId(), reference);
        }

        String transactionHash = firstString(
                data,
                "hash",
                "transactionHash",
                "txHash",
                "transactionId");
        String toAccount = firstString(
                data,
                "toAccountId",
                "toBakongAccountId",
                "receiverAccountId",
                "receiverBakongAccountId",
                "toAccount",
                "receiverAccount",
                "receiver",
                "receiverId",
                "merchantAccount");
        String transactionCurrency = firstString(
                data,
                "currency",
                "transactionCurrency");
        BigDecimal transactionAmount = firstAmount(
                data,
                "amount",
                "transactionAmount",
                "paymentAmount",
                "totalAmount");
        boolean destinationMatches = toAccount == null
                || merchantAccount == null
                || toAccount.equalsIgnoreCase(merchantAccount);
        boolean amountMatches = transactionAmount == null
                || booking.getTotalPrice() == null
                || booking.getTotalPrice().compareTo(transactionAmount) == 0;
        boolean currencyMatches = transactionCurrency == null
                || transactionCurrency.equalsIgnoreCase("USD");

        log.info(
                "Bakong reference verification: bookingId={}, md5={}, dataKeys={}, amountMatches={}, currencyMatches={}, destinationMatches={}",
                booking.getId(),
                reference,
                data.keySet(),
                amountMatches,
                currencyMatches,
                destinationMatches);

        if (!amountMatches || !currencyMatches || !destinationMatches) {
            return pendingReferenceResponse(booking.getId(), reference);
        }

        return BakongPaymentResponse.builder()
                .bookingId(booking.getId())
                .amount(transactionAmount == null ? booking.getTotalPrice() : transactionAmount)
                .currency(transactionCurrency == null ? "USD" : transactionCurrency)
                .transactionId(transactionHash)
                .md5(reference)
                .paidAt(LocalDateTime.now())
                .paymentDate(LocalDate.now())
                .status(PaymentStatus.PAID)
                .build();
    }

    private void assertCanAccessBooking(Booking booking) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Login is required to access this payment");
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            return;
        }

        Long bookingUserId = booking.getUser() == null ? null : booking.getUser().getId();

        if (bookingUserId == null || !bookingUserId.equals(user.getId())) {
            if (booking.getStatus() == org.example.vehicles_rental.enums.BookingStatus.PENDING) {
                booking.setUser(user);
                bookingRepository.save(booking);
                return;
            }

            throw new AccessDeniedException("You can only access payment for your own booking");
        }
    }

    private void validateBookingAmount(
            Booking booking,
            BigDecimal requestedAmount) {

        if (booking.getTotalPrice() == null) {
            throw new PaymentFailed("Booking total price is missing");
        }

        if (requestedAmount == null
                || booking.getTotalPrice().compareTo(requestedAmount) != 0) {
            throw new PaymentFailed("Payment amount does not match booking total");
        }
    }

    private BakongPaymentResponse pendingReferenceResponse(
            Long bookingId,
            String reference) {
        return pendingReferenceResponse(bookingId, reference, null);
    }

    private BakongPaymentResponse pendingReferenceResponse(
            Long bookingId,
            String reference,
            String message) {

        return BakongPaymentResponse.builder()
                .bookingId(bookingId)
                .md5(reference)
                .status(PaymentStatus.PENDING)
                .message(message)
                .build();
    }

    private boolean isReadyForTestPayment(Payment payment) {

        LocalDateTime createdAt = Optional.ofNullable(payment.getCreatedAt())
                .orElseGet(() -> Optional.ofNullable(payment.getExpiresAt())
                        .map(expiresAt -> expiresAt.minusMinutes(10))
                        .orElse(LocalDateTime.now()));

        return !LocalDateTime.now()
                .isBefore(createdAt.plusSeconds(TEST_PAYMENT_SUCCESS_DELAY_SECONDS));
    }

    private boolean isBakongVerificationUnavailable(Map<String, Object> result) {

        String responseCode = getString(result, "responseCode");
        String responseMessage = getString(result, "responseMessage");

        return "CONFIG_ERROR".equalsIgnoreCase(responseCode)
                || (responseMessage != null
                && responseMessage.toLowerCase().contains("api token"));
    }

    private void markPaymentPaid(Payment payment, String transactionHash) {

        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setPaymentDate(LocalDate.now());
        payment.setPaidAt(LocalDateTime.now());
        payment.getBooking().setStatus(org.example.vehicles_rental.enums.BookingStatus.CONFIRMED);

        if (transactionHash != null && !transactionHash.isBlank()) {
            payment.setBakongTransactionHash(transactionHash);
        }

        bookingRepository.save(payment.getBooking());
        paymentRepository.save(payment);
    }

    private BakongPaymentResponse buildTestPaidResponse(
            String reference,
            BigDecimal amount,
            String currency) {

        return buildTestPaidResponse(null, reference, amount, currency);
    }

    private BakongPaymentResponse buildTestPaidResponse(
            Long bookingId,
            String reference,
            BigDecimal amount,
            String currency) {

        return BakongPaymentResponse.builder()
                .bookingId(bookingId)
                .amount(amount)
                .currency(currency)
                .transactionId("TEST-" + reference)
                .md5(reference)
                .paidAt(LocalDateTime.now())
                .paymentDate(LocalDate.now())
                .status(PaymentStatus.PAID)
                .message("Bakong test payment confirmed")
                .build();
    }

    private Map<String, Object> checkTransactionByMd5(String md5) {

        String apiToken = normalizeApiToken(token);

        if (apiToken == null || apiToken.isBlank()) {
            return Map.of(
                    "responseCode", "CONFIG_ERROR",
                    "responseMessage", "Bakong API token is missing. Set BAKONG_API_TOKEN and restart backend.");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = Map.of(
                    "md5",
                    md5);

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

            log.warn(
                    "Bakong API error: {}",
                    e.getMessage());

            return null;
        }
    }

    private String normalizeApiToken(String value) {

        if (value == null) {
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

    private String getString(
            Map<?, ?> map,
            String key) {

        Object value = map.get(key);

        return value == null
                ? null
                : value.toString();
    }

    private String firstString(
            Map<?, ?> map,
            String... keys) {

        for (String key : keys) {
            String value = getString(map, key);

            if (value != null && !value.isBlank()) {
                return value;
            }
        }

        return null;
    }

    private BigDecimal getAmount(Map<?, ?> map) {

        Object value = map.get("amount");

        if (value == null) {
            return null;
        }

        try {
            return new BigDecimal(value.toString());

        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal firstAmount(
            Map<?, ?> map,
            String... keys) {

        for (String key : keys) {
            Object value = map.get(key);

            if (value == null) {
                continue;
            }

            try {
                return new BigDecimal(value.toString().replace(",", "").trim());
            } catch (NumberFormatException ignored) {
                // Try the next known Bakong amount field.
            }
        }

        return null;
    }

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
