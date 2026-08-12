package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.PaymentRequest;
import org.example.vehicles_rental.dto.response.PaymentResponse;
import org.example.vehicles_rental.entity.Booking;
import org.example.vehicles_rental.entity.Payment;
import org.example.vehicles_rental.entity.PaymentMethod;
import org.example.vehicles_rental.exception.BookingNotFound;
import org.example.vehicles_rental.exception.PaymentFailed;
import org.example.vehicles_rental.exception.PaymentMethodNotFound;
import org.example.vehicles_rental.exception.PaymentNotFound;
import org.example.vehicles_rental.repository.BookingRepository;
import org.example.vehicles_rental.repository.PaymentMethodRepository;
import org.example.vehicles_rental.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        if (request.getAmount() == null || request.
                getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentFailed("Payment amount must be greater than zero");
        }
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(request.getAmount());
        payment.setTransactionId(request.getTransactionId());
        payment.setPaymentStatus(
                request.getPaymentStatus() != null
                        ? request.getPaymentStatus()
                        : "PENDING"
        );
        payment.setPaymentDate(
                request.getPaymentDate() != null
                        ? request.getPaymentDate()
                        : java.time.LocalDate.now()
        );
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
    public PaymentResponse update(Long id, PaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFound(id));
        if (request.getBookingId() != null) {
            Booking booking = bookingRepository.findById(request.getBookingId())
                            .orElseThrow(() -> new BookingNotFound(request.getBookingId()));
            payment.setBooking(booking);
        }
        if (request.getPaymentMethodId() != null) {
            PaymentMethod paymentMethod =
                    paymentMethodRepository.findById(request.getPaymentMethodId())
                            .orElseThrow(() -> new PaymentMethodNotFound(
                                    request.getPaymentMethodId()));
            payment.setPaymentMethod(paymentMethod);
        }
        if (request.getAmount() != null) {
            if (request.getAmount()
                    .compareTo(BigDecimal.ZERO) <= 0) {
                throw new PaymentFailed("Payment amount must be greater than zero");
            }
            payment.setAmount(request.getAmount());
        }
        if (request.getTransactionId() != null) {
            payment.setTransactionId(request.getTransactionId());
        }
        if (request.getPaymentStatus() != null) {
            payment.setPaymentStatus(request.getPaymentStatus());
        }
        if (request.getPaymentDate() != null) {
            payment.setPaymentDate(request.getPaymentDate());
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
        return new PaymentResponse(
                payment.getId(),
                payment.getBooking().getId(),
                payment.getAmount(),
                payment.getPaymentMethod().getId(),
                payment.getPaymentMethod().getMethodName(),
                payment.getTransactionId(),
                payment.getPaymentStatus(),
                payment.getPaymentDate()
        );
    }
}