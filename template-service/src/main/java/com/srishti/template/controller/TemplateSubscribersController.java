package com.srishti.template.controller;

import com.srishti.template.dto.request.SubscriberListRequest;
import com.srishti.template.dto.response.TemplateResponse;
import com.srishti.template.service.TemplateSubscribersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/templates")
@RequiredArgsConstructor
public class TemplateSubscribersController {

    private final TemplateSubscribersService templateSubscribersService;

    @PostMapping("/{id}/subscribers")
    public ResponseEntity<TemplateResponse> addSubscriber(
            @RequestHeader Long ownerId,
            @PathVariable("id") Long templateId,
            @RequestBody @Valid SubscriberListRequest request) {
        return ResponseEntity.ok(templateSubscribersService.addSubscribers(ownerId, templateId, request));
    }

    @DeleteMapping("/{id}/subscribers")
    public ResponseEntity<TemplateResponse> removeSubscriber(
            @RequestHeader Long ownerId,
            @PathVariable("id") Long templateId,
            @RequestBody @Valid SubscriberListRequest request) {
        return ResponseEntity.ok(templateSubscribersService.removeSubscribers(ownerId, templateId, request));
    }
}
