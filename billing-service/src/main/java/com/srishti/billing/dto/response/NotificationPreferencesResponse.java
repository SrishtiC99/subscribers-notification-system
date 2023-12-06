package com.srishti.billing.dto.response;

public record NotificationPreferencesResponse(
        String email,
        String telegramId,
        String phoneNumber
) {
}
