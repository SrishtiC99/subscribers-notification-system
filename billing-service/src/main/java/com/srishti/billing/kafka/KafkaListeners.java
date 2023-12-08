package com.srishti.billing.kafka;

import com.srishti.billing.dto.kafka.NewAccountEventDto;
import com.srishti.billing.dto.request.BillingAccountRequest;
import com.srishti.billing.dto.request.NotificationPreferencesRequest;
import com.srishti.billing.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
@RequiredArgsConstructor
public class KafkaListeners {

    private final BillingService billingService;

    @KafkaListener(
            topics = "billing-account-topic",
            groupId = "billing-group",
            containerFactory = "newAccountKafkaListenerFactory"
    )
    public void subscriptionUpdateEventListener(NewAccountEventDto kafkaDto) {
        billingService.createAccount(kafkaDto.userId(),
                BillingAccountRequest.builder()
                        .notificationPreferences(
                                NotificationPreferencesRequest.builder()
                                        .email(kafkaDto.email())
                                        .telegramId(kafkaDto.telegramId())
                                        .phoneNumber(kafkaDto.phoneNumber())
                                        .build())
                        .accountType(kafkaDto.accountType())
                        .build());
        System.out.println("Billing account created for " + kafkaDto.accountType() + ": " + kafkaDto.userId());
    }
}
