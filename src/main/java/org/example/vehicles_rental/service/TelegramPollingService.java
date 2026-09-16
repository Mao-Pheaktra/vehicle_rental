package org.example.vehicles_rental.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.vehicles_rental.configure.TelegramConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TelegramPollingService {

    private final TelegramConfig telegramConfig;
    private final TelegramBotService telegramBotService;

    private final RestClient restClient = RestClient.create();

    private long offset = 0;

    @PostConstruct
    public void startPolling() {

        Thread pollingThread = new Thread(() -> {

            while (true) {

                try {

                    String url = "https://api.telegram.org/bot"
                            + telegramConfig.getToken()
                            + "/getUpdates"
                            + "?timeout=30"
                            + "&offset=" + offset;

                    Map response = restClient.get()
                            .uri(url)
                            .retrieve()
                            .body(Map.class);

                    if (response == null) {
                        continue;
                    }

                    Object resultObject = response.get("result");

                    if (!(resultObject instanceof Iterable<?> results)) {
                        continue;
                    }

                    for (Object item : results) {

                        if (!(item instanceof Map<?, ?> update)) {
                            continue;
                        }

                        processUpdate(update);
                    }

                } catch (Exception e) {

                    System.out.println(
                            "Telegram polling error: "
                                    + e.getMessage()
                    );

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

        });

        pollingThread.setDaemon(true);
        pollingThread.setName("telegram-polling-thread");
        pollingThread.start();

        System.out.println("Telegram polling started...");
    }

    private void processUpdate(Map<?, ?> update) {

        Object updateIdObject = update.get("update_id");

        if (updateIdObject instanceof Number number) {
            offset = number.longValue() + 1;
        }

        Object messageObject = update.get("message");

        if (!(messageObject instanceof Map<?, ?> message)) {
            return;
        }

        Object chatObject = message.get("chat");

        if (!(chatObject instanceof Map<?, ?> chat)) {
            return;
        }

        Object chatIdObject = chat.get("id");

        if (!(chatIdObject instanceof Number chatIdNumber)) {
            return;
        }

        Long chatId = chatIdNumber.longValue();

        String text = null;

        Object textObject = message.get("text");

        if (textObject != null) {
            text = textObject.toString();
        }

        telegramBotService.processUpdate(text, chatId);
    }
}