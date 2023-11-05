package com.srishti.template.dto.response;

import lombok.Builder;

@Builder
public record SubscriberResponse(
        Long id,
        String name,
        String email,
        String phoneNumber,
        String telegramId,
        GeolocationResponse geolocation
){
}
