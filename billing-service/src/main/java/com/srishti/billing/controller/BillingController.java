package com.srishti.billing.controller;

import com.srishti.billing.dto.request.BillingAccountRequest;
import com.srishti.billing.dto.response.BillingAccountResponse;
import com.srishti.billing.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/billing")
@RequiredArgsConstructor
public class BillingController {
    private final BillingService billingService;
    @PostMapping("/create")
    ResponseEntity<BillingAccountResponse> create(@RequestHeader Long ownerId,
                                  @RequestBody BillingAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(billingService.createAccount(ownerId, request));
    }

    @PostMapping("/renew")
    ResponseEntity<BillingAccountResponse> renew(@RequestHeader Long ownerId) {
        return ResponseEntity.ok(billingService.renewAccount(ownerId));
    }

    @PatchMapping("/upgrade/owner")
    ResponseEntity<BillingAccountResponse> upgradeAccountToOwner(@RequestHeader Long ownerId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(billingService.upgradeAccountToOwner(ownerId));
    }

    @GetMapping("/expiry")
    ResponseEntity<Boolean> isAccountExpired(@RequestHeader Long ownerId) {
        return ResponseEntity.ok(billingService.isAccountExpired(ownerId));
    }

    @GetMapping("/")
    ResponseEntity<BillingAccountResponse> get(@RequestHeader Long ownerId) {
        return ResponseEntity.ok(billingService.get(ownerId));
    }
}
