package com.srishti.sender.kafka;

import com.srishti.sender.dto.NotificationKafkaDto;
import com.srishti.sender.telegram.TelegramNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
@RequiredArgsConstructor
public class KafkaListeners {

    private final TelegramNotificationService telegramNotificationService;

    @KafkaListener(
            topics = "email-topic",
            groupId = "subscriber-group",
            containerFactory = "notifySubscriberKafkaListenerFactory"
    )
    public void emailNotificationListener(NotificationKafkaDto kafkaDto) {
        System.out.println(kafkaDto.credential());
    }

    @KafkaListener(
            topics = "phone-topic",
            groupId = "subscriber-group",
            containerFactory = "notifySubscriberKafkaListenerFactory"
    )
    public void phoneNotificationListener(NotificationKafkaDto kafkaDto) {
        System.out.println(kafkaDto.credential());
    }

    @KafkaListener(
            topics = "telegram-topic",
            groupId = "subscriber-group",
            containerFactory = "notifySubscriberKafkaListenerFactory"
    )
    public void telegramNotificationListener(NotificationKafkaDto kafkaDto) {
        telegramNotificationService.sendMessage(kafkaDto.credential(), kafkaDto);
    }
}
