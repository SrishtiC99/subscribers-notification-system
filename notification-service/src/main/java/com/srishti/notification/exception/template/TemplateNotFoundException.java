package com.srishti.notification.exception.template;

import jakarta.persistence.EntityNotFoundException;

public class TemplateNotFoundException extends EntityNotFoundException {

    public TemplateNotFoundException(String msg) {
        super(msg);
    }
}
