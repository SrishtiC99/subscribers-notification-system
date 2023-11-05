package com.srishti.template.service;

import com.srishti.template.client.SubscriberClient;
import com.srishti.template.dto.request.SubscriberListRequest;
import com.srishti.template.dto.response.TemplateResponse;
import com.srishti.template.entity.Template;
import com.srishti.template.exception.template.TemplateNotFoundException;
import com.srishti.template.mapper.TemplateMapper;
import com.srishti.template.repository.SubscriberIdRepository;
import com.srishti.template.repository.TemplateRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemplateSubscribersService {

    private final SubscriberClient subscriberClient;

    private final TemplateMapper mapper;

    private final TemplateRepository templateRepository;

    private final SubscriberIdRepository subscriberIdRepository;

    public TemplateResponse addSubscribers(Long ownerId, Long templateId,
                                           SubscriberListRequest request) {
        Template template = templateRepository.findByIdAndOwnerId(templateId, ownerId)
                .orElseThrow(() ->
                        new TemplateNotFoundException("template with id: " + templateId + " not found"));

        for(Long subscriberId: request.subscriberIds()) {
            if (subscriberIdRepository.existsByTemplateIdAndSubscriberId(templateId, subscriberId)) {
                log.warn("Subscriber {} already registered for template {}", subscriberId, templateId);
                continue;
            }
            try {
                Optional.of(subscriberClient.getSubscriberById(ownerId, subscriberId))
                        .map(ResponseEntity::getBody)
                                .ifPresent(subscriberResponse -> template.addSubscriber(subscriberId));
            } catch (FeignException.NotFound e) {
                log.warn("Subscriber {} not found for Owner {}", subscriberId, ownerId);
            }
        }
        templateRepository.save(template);
        return mapper.mapToResponse(template, subscriberClient);
    }

    public TemplateResponse removeSubscribers(Long ownerId, Long templateId,
                                              SubscriberListRequest request) {
        Template template = templateRepository.findByIdAndOwnerId(templateId, ownerId)
                .orElseThrow(() ->
                        new TemplateNotFoundException("template with id: " + templateId + " not found"));

        for(Long subscriberId: request.subscriberIds()) {
            if (subscriberIdRepository.existsByTemplateIdAndSubscriberId(templateId, subscriberId)) {
                Optional.of(subscriberClient.getSubscriberById(ownerId, subscriberId))
                        .map(ResponseEntity::getBody)
                        .ifPresent(subscriberResponse -> template.removeSubscriber(subscriberId));
            }
            else {
                log.warn("Subscriber {} hasn't been registered for subscriber {}", subscriberId, templateId);
            }
        }
        templateRepository.save(template);
        return mapper.mapToResponse(template, subscriberClient);
    }
}
