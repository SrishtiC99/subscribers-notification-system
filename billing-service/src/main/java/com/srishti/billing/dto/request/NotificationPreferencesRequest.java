package com.srishti.billing.dto.request;

import lombok.Builder;

@Builder
public record NotificationPreferencesRequest(
        String email,
        String telegramId,
        String phoneNumber
) {
}
