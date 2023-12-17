package com.srishti.notification.service;

import com.srishti.notification.client.TemplateClient;
import com.srishti.notification.dto.kafka.SubscriberListKafkaDto;
import com.srishti.notification.dto.request.NotificationRequest;
import com.srishti.notification.dto.response.NotificationResponse;
import com.srishti.notification.dto.response.SubscriberResponse;
import com.srishti.notification.dto.response.TemplateResponse;
import com.srishti.notification.exception.template.TemplateNotFoundException;
import com.srishti.notification.exception.template.TemplateSubscribersNotFoundException;
import com.srishti.notification.mapper.NotificationMapper;
import com.srishti.notification.repository.NotificationRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotifyKafkaEventService notifyKafkaEventService;
    private final TemplateClient templateClient;

    private final NotificationMapper mapper;

    public String sendNotificationToAllSubscribers(Long ownerId, Long templateId) {
        TemplateResponse templateResponse;
        try {
            templateResponse = templateClient.getTemplateByIdAndByOwnerId(ownerId, templateId).getBody();
        } catch (FeignException.NotFound ex) {
            throw new TemplateNotFoundException("Template not found with id: " + templateId);
        }

        List<Long> subscriberIds = templateResponse.subscriberIds()
                .stream()
                .map(SubscriberResponse::id)
                .toList();

        if(subscriberIds.size() == 0) {
            throw new TemplateSubscribersNotFoundException("No subscribers found for template: " + templateId);
        }

       notifyKafkaEventService.notifyAll(SubscriberListKafkaDto.builder()
               .ownerId(ownerId)
               .subscriberIds(subscriberIds)
               .title(templateResponse.title())
               .messageBody(templateResponse.content())
               .build());

        return "Notifications sent successfully";

    }

    public NotificationResponse createNotification(NotificationRequest request) {
        return Optional.of(request)
                .map(mapper::mapToEntity)
                .map(notificationRepository::saveAndFlush)
                .map(mapper::mapToResponse)
                .orElseThrow();
    }
}
