package com.srishti.notification.controller;

import com.srishti.notification.repository.NotificationRepository;
import com.srishti.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/{id}")
    public ResponseEntity<String> notify(
            @RequestHeader Long ownerId,
            @PathVariable("id") Long templateId
    ) {
        return ResponseEntity.ok(notificationService.sendNotificationToAllSubscribers(ownerId, templateId));
    }
}
