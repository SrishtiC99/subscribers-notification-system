package com.srishti.template.mapper;

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
            ignore = true
    )
    TemplateResponse mapToResponse(Template template); //TODO: mapping for subscriberIds
}
