package com.srishti.auth.dto.kafka;

import com.srishti.auth.model.AccountType;
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
