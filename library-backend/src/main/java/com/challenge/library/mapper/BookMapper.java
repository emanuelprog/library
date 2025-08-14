package com.challenge.library.mapper;

import com.challenge.library.dto.request.BookRequestDTO;
import com.challenge.library.dto.response.BookResponseDTO;
import com.challenge.library.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = { AuthorMapper.class, GenreMapper.class })
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book toEntity(BookRequestDTO dto);

    BookResponseDTO toResponse(Book entity);
}
