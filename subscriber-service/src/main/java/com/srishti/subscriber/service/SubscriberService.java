package com.srishti.subscriber.service;

import com.srishti.subscriber.dto.request.SubscriberRequest;
import com.srishti.subscriber.dto.response.SubscriberResponse;
import com.srishti.subscriber.entity.TemplateId;
import com.srishti.subscriber.exception.subscriber.SubscriberAlreadyExistException;
import com.srishti.subscriber.exception.subscriber.SubscriberNotFoundException;
import com.srishti.subscriber.exception.subscriber.SubscriberRegistrationException;
import com.srishti.subscriber.mapper.SubscriberMapper;
import com.srishti.subscriber.repository.SubscriberRepository;
import com.srishti.subscriber.repository.TemplateIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriberService {

    private final TemplateIdRepository templateIdRepository;

    private final SubscriberRepository subscriberRepository;

    private final SubscriberMapper mapper;

    public SubscriberResponse register(Long ownerId, SubscriberRequest request) {
        if(subscriberRepository.findByEmailAndOwnerId(request.email(), ownerId).isPresent()) {
            throw new SubscriberAlreadyExistException("Subscriber already registered");
        }

        return Optional.of(request)
                .map(mapper::mapToEntity)
                .map(subscriber -> subscriber.addOwner(ownerId))
                .map(subscriberRepository::save)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new SubscriberRegistrationException("Registration failed: " + request.email()));

    }

    public List<SubscriberResponse> getByOwner(Long ownerId) {
        return subscriberRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(mapper::mapToResponse)
                .toList();
    }

    public SubscriberResponse get(Long ownerId, Long subscriberId) {
        return subscriberRepository.findByIdAndOwnerId(subscriberId, ownerId)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new SubscriberNotFoundException("Subscriber not found: " + subscriberId));
    }

    public List<SubscriberResponse> getByTemplate(Long ownerId, Long templateId) {
        return templateIdRepository.findAllBySubscriber_ownerIdAndTemplateId(ownerId, templateId)
                .stream()
                .map(TemplateId::getSubscriber)
                .map(mapper::mapToResponse)
                .toList();
    }

    public Boolean remove(Long ownerId, Long subscriberId) {
        subscriberRepository.findByIdAndOwnerId(subscriberId, ownerId)
                        .map(subscriber -> {
                            subscriberRepository.delete(subscriber);
                            return subscriber;
                        })
                .orElseThrow(() -> new SubscriberNotFoundException("Subscriber not found: " + subscriberId));
        return true;
    }

    public SubscriberResponse update(Long ownerId, Long subscriberId, SubscriberRequest request) {
        return subscriberRepository.findByIdAndOwnerId(subscriberId, ownerId)
                .map(subscriber -> mapper.update(request, subscriber))
                .map(subscriberRepository::saveAndFlush)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new SubscriberNotFoundException("Subscriber not found: " + subscriberId));


    }
}
