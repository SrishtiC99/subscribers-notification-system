package com.srishti.sender.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class NotificationSystemBot extends TelegramLongPollingBot {

    private static final String START = "/start";

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        System.out.println(update);
        Long chatId = update.getMessage().getChatId(); // save this chatId for sending messages in future
        if (update.getMessage().getText().equals(START)) {
            sendMessage(chatId, "Welcome to NotificationSystemBot. You are eligible to receive important emergency notification from our system :)");
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
