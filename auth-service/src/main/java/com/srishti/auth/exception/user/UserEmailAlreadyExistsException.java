package com.srishti.auth.exception.user;

import jakarta.persistence.EntityExistsException;

public class UserEmailAlreadyExistsException extends EntityExistsException {

    public UserEmailAlreadyExistsException(String msg) {
        super(msg);
    }
}
