package com.srishti.billing.model;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;

@Data
@Embeddable
public class NotificationPreferences {
    private String email;
    private String telegramId;
    private String phoneNumber;
}
