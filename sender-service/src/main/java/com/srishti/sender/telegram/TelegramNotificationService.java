package com.srishti.sender.telegram;

import com.srishti.sender.dto.NotificationKafkaDto;
import com.srishti.sender.telegram.entity.TelegramChatId;
import com.srishti.sender.telegram.repository.TelegramChatIdRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class TelegramNotificationService {

    private String token = "6664874858:AAGECEyUogqdwQDYeaLDjn52JbFuXIt9OHI";

    private final TelegramApiClient telegramApiClient;

    private final TelegramChatIdRepository telegramChatIdRepository;

    public boolean sendMessage(String telegramId, NotificationKafkaDto notification) {
        System.out.println(notification);
        String title = notification.title();
        String content = notification.messageBody();

        return send(telegramId, title + ": " + content);
    }

    private boolean send(String telegramId, String message) {
        // find telegram chat id for this telegramId
        Optional<TelegramChatId> telegramChatId = telegramChatIdRepository.findByTelegramId(telegramId);
        if (telegramChatId.isEmpty()) { //TODO: Change Return Type and log any error for the customer
            log.warn("Username: " + telegramId + " not registered with telegram bot");
            return false;
        }
        Long chatId = telegramChatId.get().getChatId();
        try {
            Message telegramResponse = telegramApiClient.sendMessage(token, chatId, message);
            return true;
        } catch (FeignException.BadRequest | FeignException.Forbidden e) {
            System.out.println(e);
            return false;
        }
    }
}
