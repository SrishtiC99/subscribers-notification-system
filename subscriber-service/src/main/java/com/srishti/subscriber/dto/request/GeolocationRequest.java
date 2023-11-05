package com.srishti.subscriber.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public record GeolocationRequest (
        @DecimalMin(value = "-90", message = "latitude value can not be less than -90")
        @DecimalMax(value = "90", message = "latitude value can not be greater than 90")
        double latitude,

        @DecimalMin(value = "-180", message = "latitude value can not be less than -180")
        @DecimalMax(value = "180", message = "latitude value can not be greater than 180")
        double longitude
){
}
