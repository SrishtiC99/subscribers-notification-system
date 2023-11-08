package com.srishti.notification.dto.kafka;

import com.srishti.notification.model.NotificationStatus;
import com.srishti.notification.model.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
public record NotificationKafkaDto (  //TODO: ADD Template Info
        Long id,
        Long ownerId,
        String messageBody,
        NotificationType type,
        NotificationStatus status,
        LocalDateTime createdAt,
        Integer retryAttempts,
        String credential
){
}
