package com.srishti.billing.service;

import com.srishti.billing.dto.request.BillingAccountRequest;
import com.srishti.billing.dto.response.BillingAccountResponse;
import com.srishti.billing.mapper.BillingAccountMapper;
import com.srishti.billing.repository.BillingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;

    private final BillingAccountMapper mapper;

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
                .map(billingAccount -> billingAccount.renewAccount())
                .map(mapper::mapToResponse)
                .orElseThrow(); // TODO: Handle exception
    }
}
