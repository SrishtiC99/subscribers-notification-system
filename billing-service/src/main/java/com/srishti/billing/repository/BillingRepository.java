package com.srishti.billing.repository;

import com.srishti.billing.entity.BillingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<BillingAccount, Long> {

    Optional<BillingAccount> findByAccountHolderId(Long accountHolderId);
    Boolean existsByAccountHolderId(Long accountHolderId);
}
