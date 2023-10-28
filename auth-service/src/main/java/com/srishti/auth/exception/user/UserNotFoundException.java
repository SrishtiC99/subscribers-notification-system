package com.srishti.auth.exception.user;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
