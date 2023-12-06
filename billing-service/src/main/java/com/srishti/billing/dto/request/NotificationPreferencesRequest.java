package com.srishti.billing.dto.request;

public record NotificationPreferencesRequest(
        String email,
        String telegramId,
        String phoneNumber
) {
}
