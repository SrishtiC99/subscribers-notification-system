package com.srishti.subscriber.dto.kafka;

import lombok.Builder;

@Builder
public record NewAccountEventDto(
        Long userId,
        String email,
        String telegramId,
        String phoneNumber,
        AccountType accountType
) {
}
