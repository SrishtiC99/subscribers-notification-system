package com.srishti.template.mapper;

import com.srishti.template.client.SubscriberClient;
import com.srishti.template.dto.request.TemplateRequest;
import com.srishti.template.dto.response.TemplateResponse;
import com.srishti.template.entity.Template;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TemplateMapper extends EntityMapper<Template, TemplateRequest, TemplateResponse>{

    @Mapping(
            target = "subscriberIds",
            expression = "java(subscriberClient.getSubscribersByTemplateId(template.getOwnerId(), template.getId()).getBody())"
    )
    TemplateResponse mapToResponse(Template template, @Context SubscriberClient subscriberClient);
}
