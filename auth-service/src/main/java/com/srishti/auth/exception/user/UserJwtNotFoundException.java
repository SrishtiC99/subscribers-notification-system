package com.srishti.auth.exception.user;

import io.jsonwebtoken.JwtException;

public class UserJwtNotFoundException extends JwtException {

    public UserJwtNotFoundException(String message) {
        super(message);
    }
}
