package com.challenge.library.service;

import com.challenge.library.dto.request.AuthorRequestDTO;
import com.challenge.library.dto.response.AuthorResponseDTO;

import java.util.List;
import java.util.UUID;

public interface AuthorService {

    AuthorResponseDTO create(AuthorRequestDTO dto);

    AuthorResponseDTO update(UUID id, AuthorRequestDTO dto);

    void delete(UUID id);

    AuthorResponseDTO findById(UUID id);

    List<AuthorResponseDTO> findAll();
}
