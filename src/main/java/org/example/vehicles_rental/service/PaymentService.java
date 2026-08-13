package org.example.vehicles_rental.service;

import org.example.vehicles_rental.dto.request.PaymentRequest;
import org.example.vehicles_rental.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse create(PaymentRequest request);
    PaymentResponse getById(Long id);
    List<PaymentResponse> getAll();
    PaymentResponse getByBooking(Long bookingId);
    PaymentResponse update(Long id, PaymentRequest request);
    void delete(Long id);
}