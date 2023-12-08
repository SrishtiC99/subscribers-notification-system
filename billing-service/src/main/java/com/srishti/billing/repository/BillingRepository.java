package com.srishti.billing.repository;

import com.srishti.billing.entity.BillingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<BillingAccount, Long> {

    Optional<BillingAccount> findByAccountHolderId(Long accountHolderId);
    Boolean existsByAccountHolderId(Long accountHolderId);

    @Query(value = "SELECT account.account_holder_id FROM billing_accounts account " +
            "WHERE account.account_type = :accountType " +
            "AND account.is_expired = false " +
            "AND account.last_billing_date + INTERVAL '2 DAY' < CURRENT_TIMESTAMP", nativeQuery = true)
    List<Long> findExpiredAccounts(String accountType); //TODO: change 2 days to a real number based on subscription type
}
