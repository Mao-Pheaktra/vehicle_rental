package org.example.vehicles_rental.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.configure.TelegramConfig;
import org.example.vehicles_rental.entity.TelegramConnection;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.repository.TelegramConnectionRepository;
import org.example.vehicles_rental.repository.UserRepository;
import org.example.vehicles_rental.service.TelegramConnectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TelegramConnectionServiceImpl implements TelegramConnectionService {

    private final TelegramConnectionRepository telegramConnectionRepository;
    private final UserRepository userRepository;
    private final TelegramConfig telegramConfig;

    @Override
    @Transactional
    public String generateConnectionUrl(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete old connection code
        telegramConnectionRepository.findByUserId(userId)
                .ifPresent(telegramConnectionRepository::delete);

        telegramConnectionRepository.flush();

        String code = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        TelegramConnection connection = TelegramConnection.builder()
                .user(user)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        telegramConnectionRepository.save(connection);

        return "https://t.me/"
                + telegramConfig.getUsername()
                + "?start="
                + code;
    }
}