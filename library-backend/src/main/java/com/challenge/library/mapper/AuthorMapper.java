package com.challenge.library.mapper;

import com.challenge.library.dto.request.AuthorRequestDTO;
import com.challenge.library.dto.response.AuthorResponseDTO;
import com.challenge.library.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    Author toEntity(AuthorRequestDTO dto);

    AuthorResponseDTO toResponse(Author entity);
}