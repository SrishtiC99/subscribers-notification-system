package com.srishti.template.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record SubscriberListRequest(
        @NotEmpty(message = "Please provide at least 1 subscriber id")
        List<Long> subscriberIds
) {
}
