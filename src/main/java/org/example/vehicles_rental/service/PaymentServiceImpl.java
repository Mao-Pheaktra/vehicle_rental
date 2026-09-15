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
import org.example.vehicles_rental.enums.Role;
import org.example.vehicles_rental.exception.BookingNotFound;
import org.example.vehicles_rental.exception.NotFoundException;
import org.example.vehicles_rental.exception.PaymentMethodNotFound;
import org.example.vehicles_rental.exception.PaymentNotFound;
import org.example.vehicles_rental.repository.BookingRepository;
import org.example.vehicles_rental.repository.PaymentMethodRepository;
import org.example.vehicles_rental.repository.PaymentRepository;
import org.example.vehicles_rental.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final NotificationService  notificationService;
    private final UserRepository userRepository;

    @Override
    public PaymentResponse create(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new BookingNotFound(request.getBookingId()));

        assertCanAccessBooking(booking);

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
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDate.now());
        Payment saved = paymentRepository.save(payment);
        notificationService.notifyPaymentReceived(saved);
        return mapToResponse(saved);
    }
    @Override
    public PaymentResponse getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFound(id));
        assertCanAccessBooking(payment.getBooking());
        return mapToResponse(payment);
    }
    @Override
    public List<PaymentResponse> getAll() {
        assertAdmin();

        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public PaymentResponse getByBooking(Long bookingId){
        Payment payment = paymentRepository.findFirstByBookingIdOrderByCreatedAtDesc(bookingId).orElseThrow(
                (() -> new PaymentNotFound("Payment not found for booking:"+ bookingId))
        );
        assertCanAccessBooking(payment.getBooking());
        return mapToResponse(payment);
    }
    @Override
    public PaymentResponse update(Long id, PaymentRequest request) {
        assertAdmin();

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
        assertAdmin();
        paymentRepository.delete(payment);
    }

    private void assertCanAccessBooking(Booking booking) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                authentication instanceof AnonymousAuthenticationToken ||
                !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Login is required to access this payment");
        }

        org.example.vehicles_rental.entity.User user = userRepository.findByEmail(authentication.getName())
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

    private void assertAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                authentication instanceof AnonymousAuthenticationToken ||
                !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Admin login is required");
        }

        org.example.vehicles_rental.entity.User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Admin access is required");
        }
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking().getId())
                .paymentMethodId(payment.getPaymentMethod().getId())
                .paymentMethodName(payment.getPaymentMethod().getPaymentMethodName())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .status(payment.getPaymentStatus())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}
