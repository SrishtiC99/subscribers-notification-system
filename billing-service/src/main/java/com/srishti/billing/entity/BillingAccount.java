package com.srishti.billing.entity;

import com.srishti.billing.model.AccountType;
import com.srishti.billing.model.NotificationPreferences;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "billing_accounts"
)
public class BillingAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long accountHolderId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AccountType accountType = AccountType.USER;
    @Builder.Default
    private Boolean isExpired = false;

    @Builder.Default
    private Date lastBillingDate = new Date(System.currentTimeMillis());

    private NotificationPreferences notificationPreferences;

    public BillingAccount addAccountHolder(Long accountHolderId) {
        setAccountHolderId(accountHolderId);
        return this;
    }

    public BillingAccount renewAccount() {
        setIsExpired(false);
        setLastBillingDate(new Date(System.currentTimeMillis()));
        return this;
    }

    public BillingAccount upgradeAccount(AccountType accountType) {
        setAccountType(accountType);
        return this;
    }

}
