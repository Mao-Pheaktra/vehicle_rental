package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.admin.setting.service.NotificationService;
import org.example.vehicles_rental.dto.request.PaymentRequest;
import org.example.vehicles_rental.dto.response.PaymentResponse;
import org.example.vehicles_rental.entity.Booking;
import org.example.vehicles_rental.entity.Payment;
import org.example.vehicles_rental.entity.PaymentMethod;
import org.example.vehicles_rental.enums.PaymentMethodStatus;
import org.example.vehicles_rental.enums.PaymentStatus;
import org.example.vehicles_rental.exception.BookingNotFound;
import org.example.vehicles_rental.exception.PaymentAlreadyExists;
import org.example.vehicles_rental.exception.PaymentFailed;
import org.example.vehicles_rental.exception.PaymentMethodNotFound;
import org.example.vehicles_rental.exception.PaymentNotFound;
import org.example.vehicles_rental.repository.BookingRepository;
import org.example.vehicles_rental.repository.PaymentMethodRepository;
import org.example.vehicles_rental.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final NotificationService notificationService;

    // CREATE PAYMENT
    @Override
    public PaymentResponse create(PaymentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Payment request is required");
        }
        if (request.getBookingId() == null) {
            throw new IllegalArgumentException("Booking ID is required");
        }
        if (request.getPaymentMethodId() == null) {
            throw new IllegalArgumentException("Payment method ID is required");
        }

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new BookingNotFound(request.getBookingId()));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new PaymentMethodNotFound(request.getPaymentMethodId()));

        if (paymentMethod.getStatus() != PaymentMethodStatus.ACTIVE) {
            throw new IllegalArgumentException("Payment method is currently inactive");
        }

        // Prevent duplicate payment
        if (paymentRepository.existsByBookingId(request.getBookingId())) {
            throw new PaymentAlreadyExists(request.getBookingId());
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        // paymentDate is NOT set here.
        // It will only be set when payment becomes PAID.
        Payment saved = paymentRepository.save(payment);

        notificationService.notifyPaymentReceived(saved);
        return mapToResponse(saved);
    }

    // GET BY ID
    @Override
    public PaymentResponse getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFound(id));
        return mapToResponse(payment);
    }

    // GET ALL
    @Override
    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET BY BOOKING
    @Override
    public PaymentResponse getByBooking(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new PaymentNotFound(
                        "Payment not found for booking: " + bookingId
                ));
        return mapToResponse(payment);
    }

    // UPDATE
    @Override
    public PaymentResponse update(Long id, PaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFound(id));

        // A PAID payment must not be changed.
        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new PaymentFailed("Cannot update a payment that is already PAID");
        }

        // Update booking
        if (request.getBookingId() != null) {
            // Prevent assigning this payment to another booking that already has payment.
            if (!payment.getBooking().getId().equals(request.getBookingId())
                    && paymentRepository.existsByBookingId(request.getBookingId())) {
                throw new PaymentAlreadyExists(request.getBookingId());
            }

            Booking booking = bookingRepository.findById(request.getBookingId())
                    .orElseThrow(() -> new BookingNotFound(request.getBookingId()));

            payment.setBooking(booking);
            payment.setAmount(booking.getTotalPrice());
        }

        // Update payment method
        if (request.getPaymentMethodId() != null) {
            PaymentMethod paymentMethod = paymentMethodRepository.findById(
                    request.getPaymentMethodId()
            ).orElseThrow(() -> new PaymentMethodNotFound(
                    request.getPaymentMethodId()
            ));

            if (paymentMethod.getStatus() != PaymentMethodStatus.ACTIVE) {
                throw new IllegalArgumentException("Payment method is currently inactive");
            }

            payment.setPaymentMethod(paymentMethod);
        }

        Payment updated = paymentRepository.save(payment);
        return mapToResponse(updated);
    }

    // DELETE
    @Override
    public void delete(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFound(id));

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new PaymentFailed("Cannot delete a payment that is already PAID");
        }

        paymentRepository.delete(payment);
    }

    // MAPPER
    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking().getId())
                .paymentMethodId(payment.getPaymentMethod().getId())
                .paymentMethodName(payment.getPaymentMethod().getPaymentMethodName())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .currency(payment.getCurrency())
                .status(payment.getPaymentStatus())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}