package org.example.vehicles_rental.service;

public interface TelegramBotService {

    void sendMessage(Long chatId, String message);

    void processUpdate(String messageText, Long chatId);
}