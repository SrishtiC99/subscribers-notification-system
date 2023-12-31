package com.srishti.billing.dto.kafka;

import com.srishti.billing.model.AccountType;
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
