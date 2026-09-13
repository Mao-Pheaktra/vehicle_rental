package org.example.vehicles_rental.service;

public interface ClientTelegramService {

    void sendMessageToClient(Long userId, String message);
}