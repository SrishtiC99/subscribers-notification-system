package com.srishti.subscriber.dto.response;

import lombok.Builder;

@Builder
public record GeolocationResponse (
        double latitude,
        double longitude
){
}
