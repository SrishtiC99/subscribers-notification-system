package com.srishti.notification.mapper;

import org.mapstruct.Mapping;

import java.io.Serializable;

public interface EntityMapper<Entity, RequestDto, ResponseDto> {

    @Mapping(target = "id", ignore = true)
    Entity mapToEntity(RequestDto dto);

    ResponseDto mapToResponse(Entity entity);
}
