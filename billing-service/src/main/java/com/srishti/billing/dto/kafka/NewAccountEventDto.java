package com.srishti.billing.dto.kafka;

import lombok.Builder;

@Builder
public record NewAccountEventDto(
        Long userId,
        String email
) {
}
