package com.srishti.billing.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

public interface EntityMapper<Entity, RequestDto, ResponseDto> {

    @Mapping(target = "id", ignore = true)
    Entity mapToEntity(RequestDto request);

    ResponseDto mapToResponse(Entity entity);
}
