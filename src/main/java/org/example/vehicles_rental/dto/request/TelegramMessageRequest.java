package org.example.vehicles_rental.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TelegramMessageRequest {

    @NotBlank(message = "Message is required")
    private String message;
}