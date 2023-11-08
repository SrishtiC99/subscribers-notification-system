package com.srishti.sender.dto;

import com.srishti.sender.model.NotificationStatus;
import com.srishti.sender.model.NotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationKafkaDto(  //TODO: ADD Template Info
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
