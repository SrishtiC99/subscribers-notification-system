package com.srishti.notification.dto.response;

import lombok.Builder;

@Builder
public record GeolocationResponse(
        double latitude,
        double longitude
){
}
