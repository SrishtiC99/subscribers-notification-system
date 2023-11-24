package com.srishti.sender.telegram;

import com.srishti.sender.telegram.entity.TelegramChatId;
import com.srishti.sender.telegram.repository.TelegramChatIdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSystemBot extends TelegramLongPollingBot {

    private static final String START = "/start";

    private final TelegramChatIdRepository telegramChatIdRepository;

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        if (update.getMessage().getText().equals(START)) {
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();
            Optional<TelegramChatId> telegramChatId = telegramChatIdRepository.findByTelegramId(username);

            if(telegramChatId.isEmpty()) {
                telegramChatIdRepository.save(TelegramChatId.builder()
                        .telegramId(username)
                        .chatId(chatId)
                        .build());
            }
            sendMessage(chatId, "Welcome to NotificationSystemBot!! " +
                    "You are now registered to receive important emergency notification from our system :)");
        }
    }

    @Override
    public String getBotUsername() {
        return "KafkaNotificationBot";
    }

    @Override
    public String getBotToken() {
        return "6664874858:AAGECEyUogqdwQDYeaLDjn52JbFuXIt9OHI";
    }

    private void sendMessage(Long chatId, String text) {
        String chatIdStr = String.valueOf(chatId);
        SendMessage sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred {}: {}", chatId, e.getMessage());
        }
    }
}
