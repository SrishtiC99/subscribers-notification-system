package com.srishti.billing.dto.request;

import com.srishti.billing.model.AccountType;
import lombok.Builder;

@Builder
public record BillingAccountRequest(
        NotificationPreferencesRequest notificationPreferences,
        AccountType accountType
) {
}
