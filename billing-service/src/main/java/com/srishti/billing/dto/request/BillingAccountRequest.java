package com.srishti.billing.dto.request;

import lombok.Builder;

@Builder
public record BillingAccountRequest(
        NotificationPreferencesRequest notificationPreferences
) {
}
