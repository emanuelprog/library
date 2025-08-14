package com.challenge.library.service;

import com.challenge.library.dto.request.BookRequestDTO;
import com.challenge.library.dto.response.BookResponseDTO;

import java.util.List;
import java.util.UUID;

public interface BookService {

    BookResponseDTO create(BookRequestDTO dto);

    BookResponseDTO update(UUID id, BookRequestDTO dto);

    void delete(UUID id);

    BookResponseDTO findById(UUID id);

    List<BookResponseDTO> findAll();
}
