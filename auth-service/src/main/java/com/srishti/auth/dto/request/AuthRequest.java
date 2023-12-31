package com.srishti.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record AuthRequest (
    @NotNull(message = "{auth.email.not_null}") @Email(message = "{auth.email.invalid}")
    String email,

    @NotNull(message = "{auth.password.not_null}") @Size(min = 5, max = 25, message = "{auth.password.size}")
    String password,
    String role){
}
