package org.example.vehicles_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.service.TelegramBotService;
import org.example.vehicles_rental.service.TelegramConnectionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/telegram")
public class TelegramController {

    private final TelegramConnectionService telegramConnectionService;
    private final TelegramBotService telegramBotService;
    @PostMapping("/connect/{userId}")
    public String connectTelegram(@PathVariable Long userId) {

        return telegramConnectionService.generateConnectionUrl(userId);
    }
    @PostMapping("/test/{chatId}")
    public String testTelegram(@PathVariable Long chatId) {

        telegramBotService.sendMessage(
                chatId,
                "Hello! 👋\n\nThis is a test message from Vehicle Rental."
        );

        return "Message sent successfully";
    }
}