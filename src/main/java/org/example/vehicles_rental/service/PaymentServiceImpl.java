package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.PaymentRequest;
import org.example.vehicles_rental.dto.response.PaymentResponse;
import org.example.vehicles_rental.entity.Booking;
import org.example.vehicles_rental.entity.Payment;
import org.example.vehicles_rental.entity.PaymentMethod;
import org.example.vehicles_rental.enums.PaymentMethodStatus;
import org.example.vehicles_rental.enums.PaymentStatus;
import org.example.vehicles_rental.exception.BookingNotFound;
import org.example.vehicles_rental.exception.PaymentFailed;
import org.example.vehicles_rental.exception.PaymentMethodNotFound;
import org.example.vehicles_rental.exception.PaymentNotFound;
import org.example.vehicles_rental.repository.BookingRepository;
import org.example.vehicles_rental.repository.PaymentMethodRepository;
import org.example.vehicles_rental.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    @Override
    public PaymentResponse create(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new BookingNotFound(request.getBookingId()));
        PaymentMethod paymentMethod = paymentMethodRepository
                .findById(request.getPaymentMethodId())
                        .orElseThrow(() -> new PaymentMethodNotFound(
                                request.getPaymentMethodId()));
        if (paymentMethod.getStatus() != PaymentMethodStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Payment method is currently inactive"
            );
        }
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentStatus(String.valueOf(PaymentStatus.PENDING));
        payment.setPaymentDate(LocalDate.now());
        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved);
    }
    @Override
    public PaymentResponse getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFound(id));
        return mapToResponse(payment);
    }
    @Override
    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public PaymentResponse getByBooking(Long bookingId){
        Payment payment = paymentRepository.findByBookingId(bookingId).orElseThrow(
                (() -> new PaymentNotFound("Payment not found for booking:"+ bookingId))
        );
        return mapToResponse(payment);
    }
    @Override
    public PaymentResponse update(Long id, PaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFound(id));
        if (request.getBookingId() != null) {
            Booking booking = bookingRepository.findById(request.getBookingId())
                            .orElseThrow(() -> new BookingNotFound(request.getBookingId()));
            payment.setBooking(booking);
            payment.setAmount(booking.getTotalPrice());
        }
        if (request.getPaymentMethodId() != null) {
            PaymentMethod paymentMethod =
                    paymentMethodRepository.findById(request.getPaymentMethodId())
                            .orElseThrow(() -> new PaymentMethodNotFound(
                                    request.getPaymentMethodId()));
            payment.setPaymentMethod(paymentMethod);
        }


        Payment updated = paymentRepository.save(payment);
        return mapToResponse(updated);
    }
    @Override
    public void delete(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFound(id));
        paymentRepository.delete(payment);
    }
    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking().getId())
                .paymentMethodId(payment.getPaymentMethod().getId())
                .paymentMethodName(
                        payment.getPaymentMethod().getPaymentMethodName()
                )
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .status(PaymentStatus.valueOf(payment.getPaymentStatus()))
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}