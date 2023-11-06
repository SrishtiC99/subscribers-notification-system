package com.srishti.template.dto.kafka;

import lombok.Builder;

@Builder
public record TemplateSubscribersUpdateDto(
        Long templateId,
        Long subscriberId,
        Operation operation
){
}
