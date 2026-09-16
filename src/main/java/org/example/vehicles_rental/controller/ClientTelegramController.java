package org.example.vehicles_rental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.dto.request.TelegramMessageRequest;
import org.example.vehicles_rental.service.ClientTelegramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/clients")
public class ClientTelegramController {

    private final ClientTelegramService clientTelegramService;

    @PostMapping("/{userId}/telegram")
    public ResponseEntity<?> sendTelegramMessage(
            @PathVariable Long userId,
            @Valid @RequestBody TelegramMessageRequest request
    ) {

        clientTelegramService.sendMessageToClient(
                userId,
                request.getMessage()
        );

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Telegram message sent successfully"
                )
        );
    }
}
