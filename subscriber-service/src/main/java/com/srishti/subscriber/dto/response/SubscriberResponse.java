package com.srishti.subscriber.dto.response;

import lombok.Builder;

@Builder
public record SubscriberResponse (
        Long id,
        String name,
        String email,
        String phoneNumber,
        String telegramId,
        GeolocationResponse geolocation
){
}
