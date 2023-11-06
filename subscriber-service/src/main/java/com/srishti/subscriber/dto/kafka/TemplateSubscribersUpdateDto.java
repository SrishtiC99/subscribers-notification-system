package com.srishti.subscriber.dto.kafka;

import lombok.Builder;

@Builder
public record TemplateSubscribersUpdateDto(
        Long templateId,
        Long subscriberId,
        Operation operation
){
}
