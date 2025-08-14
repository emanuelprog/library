package com.challenge.library.service.impl;

import com.challenge.library.dto.request.AuthorRequestDTO;
import com.challenge.library.dto.response.AuthorResponseDTO;
import com.challenge.library.entity.Author;
import com.challenge.library.exception.NotFoundException;
import com.challenge.library.mapper.AuthorMapper;
import com.challenge.library.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private UUID id;
    private Author author;
    private AuthorRequestDTO request;
    private AuthorResponseDTO response;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        author = new Author();
        author.setId(id);
        author.setName("Test Author");

        request = new AuthorRequestDTO();
        request.setName("Test Author");

        response = new AuthorResponseDTO();
        response.setId(id);
        response.setName("Test Author");
    }

    @Test
    void create_ShouldReturnResponse() {
        when(authorMapper.toEntity(request)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toResponse(author)).thenReturn(response);

        AuthorResponseDTO result = authorService.create(request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void update_ShouldThrow_WhenNotFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> authorService.update(id, request));
    }

    @Test
    void update_ShouldUpdate_WhenFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toResponse(author)).thenReturn(response);

        AuthorResponseDTO result = authorService.update(id, request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void delete_ShouldThrow_WhenNotFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> authorService.delete(id));
    }

    @Test
    void delete_ShouldDelete_WhenFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        authorService.delete(id);
        verify(authorRepository).delete(author);
    }

    @Test
    void findById_ShouldReturn_WhenFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorMapper.toResponse(author)).thenReturn(response);

        AuthorResponseDTO result = authorService.findById(id);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void findById_ShouldThrow_WhenNotFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> authorService.findById(id));
    }

    @Test
    void findAll_ShouldReturnList() {
        when(authorRepository.findAll()).thenReturn(List.of(author));
        when(authorMapper.toResponse(author)).thenReturn(response);

        List<AuthorResponseDTO> result = authorService.findAll();

        assertThat(result).containsExactly(response);
    }
}