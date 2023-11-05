package com.srishti.subscriber.exception.subscriber;

import jakarta.persistence.EntityExistsException;

public class SubscriberAlreadyExistException extends EntityExistsException {

    public SubscriberAlreadyExistException(String msg) {
        super(msg);
    }
}
