package org.example.vehicles_rental.service;

import org.example.vehicles_rental.dto.request.BookingRequest;
import org.example.vehicles_rental.dto.response.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse create(BookingRequest request);
    BookingResponse getById(Long id);
    List<BookingResponse> getAll();
    BookingResponse update(Long id, BookingRequest request);
    void delete(Long id);
}