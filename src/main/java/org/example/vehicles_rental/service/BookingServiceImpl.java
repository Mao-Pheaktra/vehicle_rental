package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.BookingRequest;
import org.example.vehicles_rental.dto.response.BookingResponse;
import org.example.vehicles_rental.entity.Booking;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.entity.Vehicle;
import org.example.vehicles_rental.exception.BookingNotFound;
import org.example.vehicles_rental.exception.InvalidBooking;
import org.example.vehicles_rental.exception.UserNotFound;
import org.example.vehicles_rental.exception.VehicleNotFound;
import org.example.vehicles_rental.repository.BookingRepository;
import org.example.vehicles_rental.repository.UserRepository;
import org.example.vehicles_rental.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public BookingResponse create(BookingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFound("User not found"));
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new VehicleNotFound("Vehicle not found"));
        if (request.getPickupDate() == null || request.getReturnDate() == null) {
            throw new InvalidBooking("Pickup date and return date are required");
        }
        if (request.getReturnDate().isBefore(request.getPickupDate())) {
            throw new InvalidBooking("Return date cannot be before pickup date");
        }
        long days = ChronoUnit.DAYS.between(
                request.getPickupDate(),
                request.getReturnDate()
        );
        int totalDays = (int) Math.max(days, 1);
        BigDecimal totalPrice = vehicle.getPricePerDay()
                        .multiply(BigDecimal.valueOf(totalDays));
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setVehicle(vehicle);
        booking.setPickupDate(request.getPickupDate());
        booking.setReturnDate(request.getReturnDate());
        booking.setTotalDays(totalDays);
        booking.setTotalPrice(totalPrice);

        booking.setStatus(request.getStatus() != null ?
                request.getStatus() : "PENDING");
        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }
    @Override
    public BookingResponse getById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFound(id));
        return mapToResponse(booking);
    }
    @Override
    public List<BookingResponse> getAll() {
        return bookingRepository.findAll()
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
        if (booking.getReturnDate()
                .isBefore(booking.getPickupDate())) {
            throw new InvalidBooking("Return date cannot be before pickup date");
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
        if (request.getStatus() != null) {
            booking.setStatus(request.getStatus());
        }
        Booking updated = bookingRepository.save(booking);
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
}