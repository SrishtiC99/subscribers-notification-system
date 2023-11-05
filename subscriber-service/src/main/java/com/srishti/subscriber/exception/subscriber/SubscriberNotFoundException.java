package com.srishti.subscriber.exception.subscriber;

import jakarta.persistence.EntityNotFoundException;

public class SubscriberNotFoundException extends EntityNotFoundException {

    public SubscriberNotFoundException(String msg) {
        super(msg);
    }
}
