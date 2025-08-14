package com.challenge.library.service;

import com.challenge.library.dto.request.GenreRequestDTO;
import com.challenge.library.dto.response.GenreResponseDTO;

import java.util.List;
import java.util.UUID;

public interface GenreService {

    GenreResponseDTO create(GenreRequestDTO dto);

    GenreResponseDTO update(UUID id, GenreRequestDTO dto);

    void delete(UUID id);

    GenreResponseDTO findById(UUID id);

    List<GenreResponseDTO> findAll();
}
