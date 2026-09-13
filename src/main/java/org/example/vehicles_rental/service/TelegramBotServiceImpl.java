package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;

import org.example.vehicles_rental.configure.TelegramConfig;
import org.example.vehicles_rental.entity.TelegramConnection;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.repository.TelegramConnectionRepository;
import org.example.vehicles_rental.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {

    private final TelegramConfig telegramConfig;
    private final TelegramConnectionRepository telegramConnectionRepository;
    private final UserRepository userRepository;

    private final RestClient restClient = RestClient.create();

    @Override
    public void sendMessage(Long chatId, String message) {

        String url = "https://api.telegram.org/bot"
                + telegramConfig.getToken()
                + "/sendMessage";

        Map<String, Object> body = new HashMap<>();
        body.put("chat_id", chatId);
        body.put("text", message);

        restClient.post()
                .uri(url)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    @Transactional
    public void processUpdate(String messageText, Long chatId) {

        if (messageText == null) {
            return;
        }

        if (!messageText.startsWith("/start")) {
            sendMessage(
                    chatId,
                    "Welcome to Vehicle Rental! Please use the Connect Telegram button from the website."
            );
            return;
        }

        String[] parts = messageText.split(" ");

        if (parts.length < 2) {
            sendMessage(
                    chatId,
                    "Please connect your Telegram account from the Vehicle Rental website."
            );
            return;
        }

        String code = parts[1];

        TelegramConnection connection =
                telegramConnectionRepository.findByCode(code)
                        .orElse(null);

        if (connection == null) {
            sendMessage(
                    chatId,
                    "This connection code is invalid or has expired."
            );
            return;
        }

        if (connection.getExpiresAt().isBefore(LocalDateTime.now())) {

            telegramConnectionRepository.delete(connection);

            sendMessage(
                    chatId,
                    "This connection code has expired. Please generate a new one from the website."
            );

            return;
        }

        User user = connection.getUser();

        user.setTelegramChatId(String.valueOf(chatId));

        userRepository.save(user);

        telegramConnectionRepository.delete(connection);

        sendMessage(
                chatId,
                "Telegram connected successfully!\n\n"
                        + "You can now receive vehicle rental notifications here."
        );
    }
}