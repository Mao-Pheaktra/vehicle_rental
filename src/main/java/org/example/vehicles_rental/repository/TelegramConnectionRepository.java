package org.example.vehicles_rental.repository;

import org.example.vehicles_rental.entity.TelegramConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramConnectionRepository
        extends JpaRepository<TelegramConnection, Long> {

    Optional<TelegramConnection> findByCode(String code);

    Optional<TelegramConnection> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}