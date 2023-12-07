package com.srishti.auth.dto.kafka;

import lombok.Builder;

@Builder
public record NewAccountEventDto(
        Long userId,
        String email
) {
}
