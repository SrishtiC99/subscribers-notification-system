package com.srishti.sender.telegram;

import com.srishti.sender.dto.NotificationKafkaDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class TelegramNotificationService {

    private String token = "6664874858:AAGECEyUogqdwQDYeaLDjn52JbFuXIt9OHI";

    private final TelegramApiClient telegramApiClient;

    public boolean sendMessage(String telegramId, NotificationKafkaDto notification) {
        System.out.println(notification);
        String title = notification.title();
        String content = notification.messageBody();

        return send(telegramId, title + ": " + content);
    }

    private boolean send(String telegramId, String message) {
        try {
            Message telegramResponse = telegramApiClient.sendMessage(token, "1234605584", message);
            System.out.println("sent: " + telegramResponse);
            return true;
        } catch (FeignException.BadRequest | FeignException.Forbidden e) {
            System.out.println(e);
            return false;
        }
    }
}
