package com.srishti.auth.kafka;

import com.srishti.auth.dto.kafka.SubscriptionUpdateEventDto;
import com.srishti.auth.model.Role;
import com.srishti.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
@RequiredArgsConstructor
public class KafkaListeners {

    private final AuthService authService;

    @KafkaListener(
            topics = "billing-update-topic",
            groupId = "billing-group",
            containerFactory = "subscriptionUpdateKafkaListenerFactory"
    )
    public void subscriptionUpdateEventListener(SubscriptionUpdateEventDto kafkaDto) {
        if (kafkaDto.role().equals(Role.OWNER.name()))
            authService.updateUserRole(kafkaDto.userId(), Role.OWNER);
        else if (kafkaDto.role().equals(Role.SUBSCRIBER.name()))
            authService.updateUserRole(kafkaDto.userId(), Role.SUBSCRIBER);
        else
            authService.updateUserRole(kafkaDto.userId(), Role.USER);
    }
}
