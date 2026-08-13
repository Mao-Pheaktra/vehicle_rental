package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.PaymentMethodRequest;
import org.example.vehicles_rental.dto.response.PaymentMethodResponse;
import org.example.vehicles_rental.entity.PaymentMethod;
import org.example.vehicles_rental.enums.PaymentMethodStatus;
import org.example.vehicles_rental.exception.PaymentMethodNotFound;
import org.example.vehicles_rental.repository.PaymentMethodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;
    @Override
    public PaymentMethodResponse create(PaymentMethodRequest request) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setPaymentMethodName(request.getPaymentMethodName());
        paymentMethod.setDescription(request.getDescription());
        paymentMethod.setStatus(PaymentMethodStatus.ACTIVE);
        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);
        return mapToResponse(saved);
    }
    @Override
    public PaymentMethodResponse getById(Long id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new PaymentMethodNotFound("Payment method not found"));
        return mapToResponse(paymentMethod);
    }
    @Override
    public List<PaymentMethodResponse> getAll() {
        return paymentMethodRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public PaymentMethodResponse update(Long id, PaymentMethodRequest request) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new PaymentMethodNotFound("Payment method not found"));
        paymentMethod.setPaymentMethodName(request.getPaymentMethodName());
        paymentMethod.setDescription(request.getDescription());
        paymentMethod.setStatus(request.getStatus());
        PaymentMethod updated = paymentMethodRepository.save(paymentMethod);
        return mapToResponse(updated);
    }
    @Override
    public void delete(Long id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new PaymentMethodNotFound("Payment method not found"));
        paymentMethodRepository.delete(paymentMethod);
    }
    private PaymentMethodResponse mapToResponse(
            PaymentMethod paymentMethod) {
        return new PaymentMethodResponse(
                paymentMethod.getId(),
                paymentMethod.getPaymentMethodName(),
                paymentMethod.getDescription(),
                paymentMethod.getStatus()
        );
    }
}