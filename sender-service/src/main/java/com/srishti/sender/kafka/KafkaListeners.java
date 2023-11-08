package com.srishti.sender.kafka;

import com.srishti.sender.dto.NotificationKafkaDto;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
public class KafkaListeners {

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
        System.out.println(kafkaDto.credential());
    }
}
