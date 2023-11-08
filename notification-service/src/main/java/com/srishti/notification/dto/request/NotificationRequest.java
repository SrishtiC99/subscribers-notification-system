package com.srishti.notification.dto.request;

import com.srishti.notification.model.NotificationType;
import lombok.Builder;

@Builder
public record NotificationRequest(
        Long ownerId,
        String messageBody,
        String credential,
        NotificationType type
) {
}
