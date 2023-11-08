package com.srishti.notification.exception.template;

import jakarta.persistence.EntityNotFoundException;

public class TemplateSubscribersNotFoundException extends EntityNotFoundException {

    public TemplateSubscribersNotFoundException(String msg) {
        super(msg);
    }
}
