package org.example.vehicles_rental.service;

import org.example.vehicles_rental.dto.request.PaymentMethodRequest;
import org.example.vehicles_rental.dto.response.PaymentMethodResponse;

import java.util.List;

public interface PaymentMethodService {
    PaymentMethodResponse create(PaymentMethodRequest request);
    PaymentMethodResponse getById(Long id);
    List<PaymentMethodResponse> getAll();
    PaymentMethodResponse update(Long id, PaymentMethodRequest request);
    void delete(Long id);
}