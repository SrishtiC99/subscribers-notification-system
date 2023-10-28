package com.srishti.auth.mapper;

import com.srishti.auth.dto.request.AuthRequest;
import com.srishti.auth.entity.User;
import com.srishti.auth.model.Role;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        componentModel = "spring",
        imports = {
                Role.class
        }
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(Role.USER)")
    @Mapping(target = "password", expression = "java(encoder.encode(request.password()))")
    User mapToEntity(AuthRequest request, @Context PasswordEncoder encoder);
}
