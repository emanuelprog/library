package com.challenge.library.service.impl;

import com.challenge.library.dto.request.GenreRequestDTO;
import com.challenge.library.dto.response.GenreResponseDTO;
import com.challenge.library.entity.Genre;
import com.challenge.library.exception.AlreadyExistException;
import com.challenge.library.exception.NotFoundException;
import com.challenge.library.mapper.GenreMapper;
import com.challenge.library.repository.GenreRepository;
import com.challenge.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreResponseDTO create(GenreRequestDTO dto) {
        validateDuplicateDescription(dto.getDescription(), null);

        Genre genre = genreMapper.toEntity(dto);
        return genreMapper.toResponse(genreRepository.save(genre));
    }

    public GenreResponseDTO update(UUID id, GenreRequestDTO dto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gênero não encontrado."));

        validateDuplicateDescription(dto.getDescription(), id);

        genre.setDescription(dto.getDescription());
        return genreMapper.toResponse(genreRepository.save(genre));
    }

    public void delete(UUID id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gênero não encontrado."));
        genreRepository.delete(genre);
    }

    public GenreResponseDTO findById(UUID id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gênero não encontrado."));
        return genreMapper.toResponse(genre);
    }

    public List<GenreResponseDTO> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toResponse)
                .toList();
    }

    private void validateDuplicateDescription(String description, UUID ignoreId) {
        boolean exists = genreRepository.existsByDescriptionIgnoreCase(description.trim());

        if (exists) {
            if (ignoreId != null) {
                exists = genreRepository.findByDescriptionIgnoreCase(description.trim())
                        .filter(g -> !g.getId().equals(ignoreId))
                        .isPresent();
            }
        }

        if (exists) {
            throw new AlreadyExistException("Já existe um gênero com essa descrição.");
        }
    }
}