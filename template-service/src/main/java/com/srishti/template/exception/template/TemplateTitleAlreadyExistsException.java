package com.srishti.template.exception.template;

import jakarta.persistence.EntityExistsException;

public class TemplateTitleAlreadyExistsException extends EntityExistsException {

    public TemplateTitleAlreadyExistsException(String msg) {
        super(msg);
    }
}
