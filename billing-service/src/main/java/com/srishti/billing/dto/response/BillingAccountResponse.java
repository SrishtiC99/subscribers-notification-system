package com.srishti.billing.dto.response;

import com.srishti.billing.model.AccountType;

import java.util.Date;

public record BillingAccountResponse(
        String id,
        String accountHolderId,
        AccountType accountType,
        Boolean isExpired,
        Date lastBillingDate,
        NotificationPreferencesResponse notificationPreferences
) {
}
