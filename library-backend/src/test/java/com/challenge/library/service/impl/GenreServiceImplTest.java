package com.challenge.library.service.impl;

import com.challenge.library.dto.request.GenreRequestDTO;
import com.challenge.library.dto.response.GenreResponseDTO;
import com.challenge.library.entity.Genre;
import com.challenge.library.exception.AlreadyExistException;
import com.challenge.library.exception.NotFoundException;
import com.challenge.library.mapper.GenreMapper;
import com.challenge.library.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    private final GenreMapper genreMapper = Mappers.getMapper(GenreMapper.class);

    private GenreServiceImpl genreService;

    private UUID id;
    private Genre genre;
    private GenreRequestDTO request;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        genreService = new GenreServiceImpl(genreRepository, genreMapper);

        id = UUID.randomUUID();
        genre = new Genre();
        genre.setId(id);
        genre.setDescription("Drama");

        request = new GenreRequestDTO();
        request.setDescription("Drama");
    }

    @Test
    void create_ShouldReturnResponse() {
        when(genreRepository.existsByDescriptionIgnoreCase("Drama")).thenReturn(false);
        when(genreRepository.save(any(Genre.class))).thenReturn(genre);

        GenreResponseDTO result = genreService.create(request);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDescription()).isEqualTo("Drama");
    }

    @Test
    void create_ShouldThrow_WhenDescriptionExists() {
        when(genreRepository.existsByDescriptionIgnoreCase("Drama")).thenReturn(true);

        assertThrows(AlreadyExistException.class, () -> genreService.create(request));
    }

    @Test
    void update_ShouldUpdate_WhenFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.of(genre));
        when(genreRepository.existsByDescriptionIgnoreCase("Romance")).thenReturn(false);
        when(genreRepository.save(any(Genre.class))).thenAnswer(invocation -> {
            Genre saved = invocation.getArgument(0);
            saved.setId(id);
            return saved;
        });

        GenreRequestDTO updateRequest = new GenreRequestDTO();
        updateRequest.setDescription("Romance");

        GenreResponseDTO result = genreService.update(id, updateRequest);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDescription()).isEqualTo("Romance");
    }

    @Test
    void update_ShouldThrow_WhenNotFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> genreService.update(id, request));
    }

    @Test
    void update_ShouldThrow_WhenDuplicateDescription() {
        when(genreRepository.findById(id)).thenReturn(Optional.of(genre));
        when(genreRepository.existsByDescriptionIgnoreCase("Drama")).thenReturn(true);

        Genre anotherGenre = new Genre();
        anotherGenre.setId(UUID.randomUUID());
        anotherGenre.setDescription("Drama");

        when(genreRepository.findByDescriptionIgnoreCase("Drama"))
                .thenReturn(Optional.of(anotherGenre));

        assertThrows(AlreadyExistException.class, () -> genreService.update(id, request));
    }

    @Test
    void delete_ShouldDelete_WhenFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.of(genre));

        genreService.delete(id);

        verify(genreRepository).delete(genre);
    }

    @Test
    void delete_ShouldThrow_WhenNotFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> genreService.delete(id));
    }

    @Test
    void findById_ShouldReturn_WhenFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.of(genre));

        GenreResponseDTO result = genreService.findById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDescription()).isEqualTo("Drama");
    }

    @Test
    void findById_ShouldThrow_WhenNotFound() {
        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> genreService.findById(id));
    }

    @Test
    void findAll_ShouldReturnList() {
        when(genreRepository.findAll()).thenReturn(List.of(genre));

        List<GenreResponseDTO> result = genreService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(id);
        assertThat(result.getFirst().getDescription()).isEqualTo("Drama");
    }
}