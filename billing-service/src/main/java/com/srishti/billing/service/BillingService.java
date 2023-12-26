package com.srishti.billing.service;

import com.srishti.billing.dto.kafka.SubscriptionUpdateEventDto;
import com.srishti.billing.dto.request.BillingAccountRequest;
import com.srishti.billing.dto.response.BillingAccountResponse;
import com.srishti.billing.entity.BillingAccount;
import com.srishti.billing.mapper.BillingAccountMapper;
import com.srishti.billing.model.AccountType;
import com.srishti.billing.repository.BillingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final BillingRepository billingRepository;

    private final BillingAccountMapper mapper;

    private final KafkaTemplate<String, SubscriptionUpdateEventDto> template;

    private String SUBSCRIPTION_UPDATE_TOPIC = "billing-update-topic";

    public BillingAccountResponse createAccount(Long ownerId, BillingAccountRequest request) {
        if (billingRepository.existsByAccountHolderId(ownerId)) {
            return renewAccount(ownerId);
        }
        return Optional.of(request)
                .map(mapper::mapToEntity)
                .map(billingAccount -> billingAccount.addAccountHolder(ownerId))
                .map(billingRepository::save)
                .map(mapper::mapToResponse)
                .orElseThrow(); // TODO: Handle exception
    }

    public BillingAccountResponse renewAccount(Long ownerId) {
        return billingRepository.findByAccountHolderId(ownerId)
                .map(BillingAccount::renewAccount)
                .map(billingRepository::save)
                .map(mapper::mapToResponse)
                .orElseThrow(); // TODO: Handle exception
        //TODO: Send a notification to the user that their subscription has been renewed
    }

    public BillingAccountResponse upgradeAccountToOwner(Long userId) {
        // TODO: send notification to user about this update
        template.send(SUBSCRIPTION_UPDATE_TOPIC, SubscriptionUpdateEventDto.builder()
                        .userId(userId)
                        .role(AccountType.OWNER.name())
                .build());

        return billingRepository.findByAccountHolderId(userId)
                .map(billingAccount -> billingAccount.upgradeAccount(AccountType.OWNER))
                .map(billingRepository::save)
                .map(mapper::mapToResponse)
                .orElseThrow(); // TODO: Handle exception
    }

    public Boolean isAccountExpired(Long ownerId) {
        return billingRepository.findByAccountHolderId(ownerId)
                .get().getIsExpired();
    }

    @Scheduled(cron = "0 0 22 * * *", zone = "Asia/Kolkata")
    public void suspendExpiredOwnersAccount() {
        log.info("suspendExpiredOwnersAccount() task started");
        List<Long> expiredOwnerIds = billingRepository.findExpiredAccounts(AccountType.OWNER.toString());
        for(Long id: expiredOwnerIds) {
            billingRepository.findByAccountHolderId(id)
                    .map(billingAccount -> {
                        billingAccount.setIsExpired(true);
                        return billingAccount;
                    })
                    .map(billingRepository::save);
            log.warn("Owner Account suspended: {}", id);
            //TODO: Send a notification to the user for subscription renewal
        }
    }

    public BillingAccountResponse get(Long ownerId) {
        return billingRepository.findByAccountHolderId(ownerId)
                .map(mapper::mapToResponse)
                .orElseThrow();
    }
}
