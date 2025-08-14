package com.challenge.library.mapper;

import com.challenge.library.dto.request.GenreRequestDTO;
import com.challenge.library.dto.response.GenreResponseDTO;
import com.challenge.library.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    Genre toEntity(GenreRequestDTO dto);

    GenreResponseDTO toResponse(Genre entity);
}
