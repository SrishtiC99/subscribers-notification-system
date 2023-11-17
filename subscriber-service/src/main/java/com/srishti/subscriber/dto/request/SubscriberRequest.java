package com.srishti.subscriber.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SubscriberRequest (
        @Size(max = 50, message = "name is too long")
        String name,
        @NotNull(message = "Email can not be NULL") @Email(message = "invalid email") @Size(max = 50, message = "email is too long")
        String email,
        @Size(max = 20, message = "invalid number")
        String phoneNumber,
        @Size(max = 20, message = "telegram id too long")
        String telegramId,
        @Valid
        GeolocationRequest geolocation
){
}
