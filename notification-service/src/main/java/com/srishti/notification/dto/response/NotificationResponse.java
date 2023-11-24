package com.srishti.notification.dto.response;

import com.srishti.notification.model.NotificationStatus;
import com.srishti.notification.model.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        Long ownerId,
        String credential,
        String title,
        String messageBody,
        Integer retryAttempts,
        LocalDateTime createdAt,
        NotificationType type,
        NotificationStatus status
) {
}
