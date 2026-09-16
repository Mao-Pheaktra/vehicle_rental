package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.vehicles_rental.admin.setting.service.NotificationService;
import org.example.vehicles_rental.dto.request.BookingRequest;
import org.example.vehicles_rental.dto.response.BookingResponse;
import org.example.vehicles_rental.entity.Booking;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.entity.Vehicle;
import org.example.vehicles_rental.enums.BookingStatus;
import org.example.vehicles_rental.exception.BookingNotFound;
import org.example.vehicles_rental.exception.InvalidBooking;
import org.example.vehicles_rental.exception.UserNotFound;
import org.example.vehicles_rental.exception.VehicleNotFound;
import org.example.vehicles_rental.repository.BookingRepository;
import org.example.vehicles_rental.repository.UserRepository;
import org.example.vehicles_rental.repository.VehicleRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final NotificationService notificationService;
    private final TelegramNotificationService telegramNotificationService;

    @Override
    public BookingResponse create(BookingRequest request) {
        User user = getAuthenticatedUser();
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new VehicleNotFound("Vehicle not found"));
       if (!request.getPickupDate().isBefore(request.getReturnDate())){
           throw new InvalidBooking("Return date must be after pickup date");
       }
        boolean alreadyBooked = bookingRepository.existsOverlappingBookingForOtherUser(
                request.getVehicleId(),
                user.getId(),
                request.getPickupDate(),
                request.getReturnDate(),
                List.of(
                        BookingStatus.PENDING,
                        BookingStatus.CONFIRMED
                )
        );
       if (alreadyBooked){
           throw new InvalidBooking("Vehicle is already booked for these dates");
       }
        int totalDays = Math.toIntExact(ChronoUnit.DAYS.between(
                request.getPickupDate(),
                request.getReturnDate()
        ));

        BigDecimal totalPrice = vehicle.getPricePerDay()
                        .multiply(BigDecimal.valueOf(totalDays));
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setVehicle(vehicle);
        booking.setPickupDate(request.getPickupDate());
        booking.setReturnDate(request.getReturnDate());
        booking.setTotalDays(totalDays);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);
        Booking saved = bookingRepository.save(booking);
        try {
            notificationService.notifyNewBooking(saved);
        } catch (RuntimeException e) {
            log.warn("Booking notification failed for bookingId={}: {}", saved.getId(), e.getMessage());
        }

        return mapToResponse(saved);

       
    }
    @Override
    public BookingResponse getById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFound(id));
        assertCanAccessBooking(booking);
        return mapToResponse(booking);
    }
    @Override
    public List<BookingResponse> getAll() {
        User user = getAuthenticatedUser();
        List<Booking> bookings = user.getRole() == org.example.vehicles_rental.enums.Role.ADMIN
                ? bookingRepository.findAll()
                : bookingRepository.findByUserId(user.getId());

        return bookings
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public BookingResponse update(Long id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFound(id));
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new UserNotFound("User not found"));
            booking.setUser(user);
        }
        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                    .orElseThrow(() -> new VehicleNotFound("Vehicle not found"));
            booking.setVehicle(vehicle);
        }
        if (request.getPickupDate() != null) {
            booking.setPickupDate(request.getPickupDate());
        }
        if (request.getReturnDate() != null) {
            booking.setReturnDate(request.getReturnDate());
        }
        if (booking.getPickupDate() == null || booking.getReturnDate() == null) {
            throw new InvalidBooking("Pickup date and return date are required");
        }
        if (!booking.getReturnDate()
                .isAfter(booking.getPickupDate())) {
            throw new InvalidBooking(
                    "Return date must be after pickup date"
            );
        }
        // Check vehicle availability
        boolean alreadyBooked =
                bookingRepository.existsOverlappingBookingForOtherUserOnUpdate(
                        booking.getVehicle().getId(),
                        booking.getId(),
                        booking.getUser().getId(),
                        booking.getPickupDate(),
                        booking.getReturnDate(),
                        List.of(
                                BookingStatus.PENDING,
                                BookingStatus.CONFIRMED
                        )
                );

        if (alreadyBooked) {
            throw new InvalidBooking(
                    "Vehicle is already booked for these dates"
            );
        }
        long days = ChronoUnit.DAYS.between(
                booking.getPickupDate(),
                booking.getReturnDate()
        );
        int totalDays = (int) Math.max(days, 1);
        booking.setTotalDays(totalDays);
        BigDecimal totalPrice = booking.getVehicle()
                        .getPricePerDay()
                        .multiply(BigDecimal.valueOf(totalDays));
        booking.setTotalPrice(totalPrice);
        BookingStatus oldStatus = booking.getStatus();

        if (request.getStatus() != null) {
            booking.setStatus(request.getStatus());
        }
        Booking updated = bookingRepository.save(booking);
        if (oldStatus != BookingStatus.CANCELLED
                && updated.getStatus() == BookingStatus.CANCELLED) {

            notificationService.notifyBookingCancellation(updated);
        }

        return mapToResponse(updated);
    }
    @Override
    public void delete(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFound(id));
        bookingRepository.delete(booking);
    }
    
    private BookingResponse mapToResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUser().getId(),
                booking.getUser().getName(),
                booking.getVehicle().getId(),
                booking.getVehicle().getName(),
                booking.getPickupDate(),
                booking.getReturnDate(),
                booking.getTotalDays(),
                booking.getTotalPrice(),
                booking.getStatus()
        );
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Login is required to access bookings");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFound("User not found"));
    }

    private void assertCanAccessBooking(Booking booking) {
        User user = getAuthenticatedUser();

        if (user.getRole() == org.example.vehicles_rental.enums.Role.ADMIN) {
            return;
        }

        Long bookingUserId = booking.getUser() == null ? null : booking.getUser().getId();

        if (bookingUserId == null || !bookingUserId.equals(user.getId())) {
            throw new AccessDeniedException("You can only access your own booking");
        }
    }
}
