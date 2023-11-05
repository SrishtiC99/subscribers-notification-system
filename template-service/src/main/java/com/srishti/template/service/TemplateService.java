package com.srishti.template.service;

import com.srishti.template.client.SubscriberClient;
import com.srishti.template.dto.request.TemplateRequest;
import com.srishti.template.dto.response.TemplateResponse;
import com.srishti.template.entity.Template;
import com.srishti.template.exception.template.TemplateCreationException;
import com.srishti.template.exception.template.TemplateNotFoundException;
import com.srishti.template.exception.template.TemplateTitleAlreadyExistsException;
import com.srishti.template.mapper.TemplateMapper;
import com.srishti.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;

    private final TemplateMapper mapper;

    private final SubscriberClient subscriberClient;

    public TemplateResponse create(Long ownerId, TemplateRequest request) {

        if(templateRepository.existsTemplateByOwnerIdAndTitle(ownerId, request.title())) {
            throw new TemplateTitleAlreadyExistsException("Template title: " + request.title() + " already exists");
        }

        return Optional.of(request)
                .map(mapper::mapToEntity)
                .map(template -> template.addOwner(ownerId))
                .map(templateRepository::save)
                .map(template -> mapper.mapToResponse(template, subscriberClient))
                .orElseThrow(() -> new TemplateCreationException("msg"));
    }

    public TemplateResponse get(Long ownerId, Long templateId) {

        return templateRepository.findByIdAndOwnerId(templateId, ownerId)
                .map(template -> mapper.mapToResponse(template, subscriberClient))
                .orElseThrow(() -> new TemplateNotFoundException("template not found with id: " + templateId));
    }

    public Boolean delete(Long ownerId, Long templateId) {
        Template template = templateRepository.findByIdAndOwnerId(templateId, ownerId)
                .orElseThrow(() -> new TemplateNotFoundException("template not found with id: " + templateId));

        templateRepository.delete(template);
        return true;
    }
}
