package com.challenge.library.service.impl;

import com.challenge.library.dto.request.GenreRequestDTO;
import com.challenge.library.dto.response.GenreResponseDTO;
import com.challenge.library.entity.Genre;
import com.challenge.library.exception.NotFoundException;
import com.challenge.library.mapper.GenreMapper;
import com.challenge.library.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @InjectMocks
    private GenreServiceImpl genreService;

    private UUID id;
    private Genre genre;
    private GenreRequestDTO request;
    private GenreResponseDTO response;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        genre = new Genre();
        genre.setId(id);
        request = new GenreRequestDTO();
        response = new GenreResponseDTO();
        response.setId(id);
    }

    @Test
    void create_ShouldReturnResponse() {
        when(genreMapper.toEntity(request)).thenReturn(genre);
        when(genreRepository.save(genre)).thenReturn(genre);
        when(genreMapper.toResponse(genre)).thenReturn(response);

        GenreResponseDTO result = genreService.create(request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void update_ShouldThrow_WhenNotFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> genreService.update(id, request));
    }

    @Test
    void update_ShouldReturn_WhenFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.of(genre));
        when(genreRepository.save(genre)).thenReturn(genre);
        when(genreMapper.toResponse(genre)).thenReturn(response);

        GenreResponseDTO result = genreService.update(id, request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void delete_ShouldThrow_WhenNotFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> genreService.delete(id));
    }

    @Test
    void delete_ShouldDelete_WhenFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.of(genre));
        genreService.delete(id);
        verify(genreRepository).delete(genre);
    }

    @Test
    void findById_ShouldThrow_WhenNotFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> genreService.findById(id));
    }

    @Test
    void findById_ShouldReturn_WhenFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.of(genre));
        when(genreMapper.toResponse(genre)).thenReturn(response);

        GenreResponseDTO result = genreService.findById(id);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void findAll_ShouldReturnList() {
        when(genreRepository.findAll()).thenReturn(List.of(genre));
        when(genreMapper.toResponse(genre)).thenReturn(response);

        List<GenreResponseDTO> result = genreService.findAll();

        assertThat(result).containsExactly(response);
    }
}