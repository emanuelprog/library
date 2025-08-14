package com.challenge.library.mapper;

import com.challenge.library.dto.request.UserRequestDTO;
import com.challenge.library.dto.response.UserResponseDTO;
import com.challenge.library.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserRequestDTO dto);

    UserResponseDTO toResponse(User entity);
}
