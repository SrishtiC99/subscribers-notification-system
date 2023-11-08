package com.srishti.notification.dto.kafka;

import lombok.Builder;

import java.util.List;

@Builder
public record SubscriberListKafkaDto(
        Long ownerId,
        List<Long> subscriberIds,
        String messageBody
) {
}
