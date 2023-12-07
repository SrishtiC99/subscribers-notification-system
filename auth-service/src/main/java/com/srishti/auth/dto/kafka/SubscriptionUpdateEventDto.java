package com.srishti.auth.dto.kafka;

import lombok.Builder;

@Builder
public record SubscriptionUpdateEventDto(
        Long userId,
        String role
) {
}
