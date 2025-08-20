package com.challenge.library.service.impl;

import com.challenge.library.dto.request.AuthorRequestDTO;
import com.challenge.library.dto.response.AuthorResponseDTO;
import com.challenge.library.entity.Author;
import com.challenge.library.exception.AlreadyExistException;
import com.challenge.library.exception.NotFoundException;
import com.challenge.library.mapper.AuthorMapper;
import com.challenge.library.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    private final AuthorMapper authorMapper = Mappers.getMapper(AuthorMapper.class);

    private AuthorServiceImpl authorService;

    private UUID id;
    private Author author;
    private AuthorRequestDTO request;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        authorService = new AuthorServiceImpl(authorRepository, authorMapper);

        id = UUID.randomUUID();
        author = new Author();
        author.setId(id);
        author.setName("Test Author");

        request = new AuthorRequestDTO();
        request.setName("Test Author");
    }

    @Test
    void create_ShouldReturnResponse() {
        when(authorRepository.existsByNameIgnoreCase("Test Author")).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorResponseDTO result = authorService.create(request);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Test Author");
    }

    @Test
    void create_ShouldThrow_WhenNameExists() {
        when(authorRepository.existsByNameIgnoreCase("Test Author")).thenReturn(true);

        assertThrows(AlreadyExistException.class, () -> authorService.create(request));
    }

    @Test
    void update_ShouldUpdate_WhenFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorRepository.existsByNameIgnoreCase("Nome Atualizado")).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> {
            Author saved = invocation.getArgument(0);
            saved.setId(id);
            return saved;
        });

        AuthorRequestDTO updateRequest = new AuthorRequestDTO();
        updateRequest.setName("Nome Atualizado");

        AuthorResponseDTO result = authorService.update(id, updateRequest);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Nome Atualizado");
    }

    @Test
    void update_ShouldThrow_WhenNotFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.update(id, request));
    }

    @Test
    void update_ShouldThrow_WhenDuplicateName() {
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorRepository.existsByNameIgnoreCase("Test Author")).thenReturn(true);
        Author anotherAuthor = new Author();
        anotherAuthor.setId(UUID.randomUUID());
        anotherAuthor.setName("Test Author");

        when(authorRepository.findByNameIgnoreCase("Test Author"))
                .thenReturn(Optional.of(anotherAuthor));

        assertThrows(AlreadyExistException.class, () -> authorService.update(id, request));
    }

    @Test
    void delete_ShouldDelete_WhenFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));

        authorService.delete(id);

        verify(authorRepository).delete(author);
    }

    @Test
    void delete_ShouldThrow_WhenNotFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.delete(id));
    }

    @Test
    void findById_ShouldReturn_WhenFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));

        AuthorResponseDTO result = authorService.findById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Test Author");
    }

    @Test
    void findById_ShouldThrow_WhenNotFound() {
        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authorService.findById(id));
    }

    @Test
    void findAll_ShouldReturnList() {
        when(authorRepository.findAll()).thenReturn(List.of(author));

        List<AuthorResponseDTO> result = authorService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(id);
    }
}