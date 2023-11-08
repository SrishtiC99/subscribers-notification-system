package com.srishti.notification.client;

import com.srishti.notification.dto.response.SubscriberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "subscriber-service")
public interface SubscriberClient {

    @GetMapping("api/v1/subscribers/{id}")
    ResponseEntity<SubscriberResponse> getSubscriberById(
            @RequestHeader Long ownerId,
            @PathVariable("id") Long subscriberId);
}
