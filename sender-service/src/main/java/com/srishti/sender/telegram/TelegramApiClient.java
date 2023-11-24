package com.srishti.sender.telegram;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.telegram.telegrambots.meta.api.objects.Message;

@FeignClient(url = "https://api.telegram.org/", name = "TelegramAPI")
public interface TelegramApiClient {

    @PostMapping(value = "/bot{token}/sendMessage")
    Message sendMessage(@PathVariable String token,
                        @RequestParam("chat_id") Long chatId,
                        @RequestParam("text") String content);
}
