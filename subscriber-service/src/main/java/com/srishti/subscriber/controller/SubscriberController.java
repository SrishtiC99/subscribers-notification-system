package com.srishti.subscriber.controller;

import com.srishti.subscriber.dto.request.SubscriberRequest;
import com.srishti.subscriber.dto.response.SubscriberResponse;
import com.srishti.subscriber.service.SubscriberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscribers")
public class SubscriberController {

    private final SubscriberService subscriberService;

    @PostMapping("/")
    public ResponseEntity<SubscriberResponse> register(
            @RequestHeader Long ownerId,
            @RequestBody @Valid SubscriberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriberService.register(ownerId, request));
    }

    @GetMapping("/")
    public ResponseEntity<List<SubscriberResponse>> getByOwnerId(
            @RequestHeader Long ownerId) {
        return ResponseEntity.ok(subscriberService.getByOwner(ownerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriberResponse> getById(
            @RequestHeader Long ownerId,
            @PathVariable("id") Long subscriberId) {
        return ResponseEntity.ok(subscriberService.get(ownerId, subscriberId));
    }

    @GetMapping("/template/{id}")
    public ResponseEntity<List<SubscriberResponse>> getByTemplateId(
            @RequestHeader Long ownerId,
            @PathVariable("id") Long templateId) {
        return ResponseEntity.ok(subscriberService.getByTemplate(ownerId, templateId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> remove(
            @RequestHeader Long ownerId,
            @PathVariable("id") Long subscriberId) {
        return ResponseEntity.ok(subscriberService.remove(ownerId, subscriberId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriberResponse> update(
            @RequestHeader Long ownerId,
            @PathVariable("id") Long subscriberId,
            @RequestBody @Valid SubscriberRequest request) {
        return ResponseEntity.ok(subscriberService.update(ownerId, subscriberId, request));
    }
}

