package com.srishti.auth.exception.user;

import org.springframework.security.authentication.BadCredentialsException;

public class UserBadCredentialsException extends BadCredentialsException {

    public UserBadCredentialsException(String msg) {
        super(msg);
    }
}
