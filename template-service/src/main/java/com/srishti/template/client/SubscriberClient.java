package com.srishti.template.client;

import com.srishti.template.dto.response.SubscriberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "subscriber-service")
public interface SubscriberClient {

    @GetMapping("/api/v1/subscribers/{id}")
    ResponseEntity<SubscriberResponse> getSubscriberById(
            @RequestHeader Long ownerId,
            @PathVariable("id") Long subscriberId);

    @GetMapping("/api/v1/subscribers/template/{id}")
    ResponseEntity<List<SubscriberResponse>> getSubscribersByTemplateId(
            @RequestHeader Long ownerId,
            @PathVariable("id") Long templateId);
}
