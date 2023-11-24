package com.srishti.notification.service;

import com.srishti.notification.client.SubscriberClient;
import com.srishti.notification.dto.kafka.NotificationKafkaDto;
import com.srishti.notification.dto.kafka.SubscriberListKafkaDto;
import com.srishti.notification.dto.request.NotificationRequest;
import com.srishti.notification.dto.response.NotificationResponse;
import com.srishti.notification.dto.response.SubscriberResponse;
import com.srishti.notification.mapper.NotificationMapper;
import com.srishti.notification.model.NotificationType;
import com.srishti.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class NotifyKafkaEventService {

    private final KafkaTemplate<String, NotificationKafkaDto> kafkaTemplate;

    private final NotificationService notificationService;

    private final SubscriberClient subscriberClient;

    private final NotificationMapper mapper;

    private String EMAIL_TOPIC = "email-topic";
    private String PHONE_TOPIC = "phone-topic";
    private String TELEGRAM_TOPIC = "telegram-topic";

    public void notifyAll(SubscriberListKafkaDto kafkaDto) {
        Long ownerId = kafkaDto.ownerId();

        for (Long subscriberId: kafkaDto.subscriberIds()) {
            SubscriberResponse subscriberResponse = subscriberClient.getSubscriberById(ownerId, subscriberId).getBody();
            sendNotificationByCredential(subscriberResponse::email, NotificationType.EMAIL,
                    ownerId, kafkaDto.title(), kafkaDto.messageBody(), EMAIL_TOPIC);
            sendNotificationByCredential(subscriberResponse::phoneNumber, NotificationType.PHONE,
                    ownerId, kafkaDto.title(), kafkaDto.messageBody(), PHONE_TOPIC);
            sendNotificationByCredential(subscriberResponse::telegramId, NotificationType.TELEGRAM,
                    ownerId, kafkaDto.title(), kafkaDto.messageBody(), TELEGRAM_TOPIC);
        }
    }

    private void sendNotificationByCredential(
            Supplier<String> supplier,
            NotificationType type,
            Long ownerId,
            String title,
            String body,
            String topic
            ) {
        String credential = supplier.get();

        if(credential != null) {
            // create a notification
            NotificationResponse notificationResponse = notificationService.createNotification(
                    NotificationRequest.builder()
                            .ownerId(ownerId)
                            .title(title)
                            .messageBody(body)
                            .credential(credential)
                            .type(type)
                            .build());
            // map to kafka notification
            NotificationKafkaDto kafkaDto = mapper.mapToKafka(notificationResponse);
            System.out.println("sending notification: " + kafkaDto);
            kafkaTemplate.send(topic, kafkaDto);
        }
    }
}
