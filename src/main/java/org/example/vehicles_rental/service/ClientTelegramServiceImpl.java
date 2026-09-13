package org.example.vehicles_rental.service;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.entity.User;
import org.example.vehicles_rental.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientTelegramServiceImpl implements ClientTelegramService {

    private final UserRepository userRepository;
    private final TelegramBotService telegramBotService;

    @Override
    public void sendMessageToClient(Long userId, String message) {

        System.out.println("===== SEND TELEGRAM TO CLIENT =====");
        System.out.println("User ID: " + userId);
        System.out.println("Message: " + message);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Client not found"));

        System.out.println("Client found: " + user.getName());

        String telegramChatId = user.getTelegramChatId();

        System.out.println("Telegram Chat ID: " + telegramChatId);

        if (telegramChatId == null || telegramChatId.isBlank()) {
            throw new RuntimeException(
                    "This client has not connected Telegram"
            );
        }

        Long chatId;

        try {
            chatId = Long.parseLong(telegramChatId);
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    "Invalid Telegram chat ID"
            );
        }

        System.out.println("Parsed Chat ID: " + chatId);

        telegramBotService.sendMessage(
                chatId,
                message
        );

        System.out.println("Telegram sendMessage() completed");
    }
}