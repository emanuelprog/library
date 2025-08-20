package com.challenge.library.service.impl;

import com.challenge.library.dto.request.BookRequestDTO;
import com.challenge.library.dto.response.BookResponseDTO;
import com.challenge.library.entity.Book;
import com.challenge.library.exception.AlreadyExistException;
import com.challenge.library.exception.NotFoundException;
import com.challenge.library.mapper.AuthorMapper;
import com.challenge.library.mapper.BookMapperImpl;
import com.challenge.library.mapper.GenreMapper;
import com.challenge.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private GenreMapper genreMapper;

    private BookServiceImpl bookService;

    private UUID id;
    private Book book;
    private BookRequestDTO request;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        BookMapperImpl bookMapper = new BookMapperImpl();

        ReflectionTestUtils.setField(bookMapper, "authorMapper", authorMapper);
        ReflectionTestUtils.setField(bookMapper, "genreMapper", genreMapper);

        bookService = new BookServiceImpl(bookRepository, bookMapper);

        id = UUID.randomUUID();
        book = new Book();
        book.setId(id);
        book.setTitle("Test Book");

        request = new BookRequestDTO();
        request.setTitle("Test Book");
    }

    @Test
    void create_ShouldReturnResponse() {
        when(bookRepository.existsByTitleIgnoreCase("Test Book")).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponseDTO result = bookService.create(request);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getTitle()).isEqualTo("Test Book");
    }

    @Test
    void create_ShouldThrow_WhenTitleExists() {
        when(bookRepository.existsByTitleIgnoreCase("Test Book")).thenReturn(true);

        assertThrows(AlreadyExistException.class, () -> bookService.create(request));
    }

    @Test
    void update_ShouldUpdate_WhenFound() {
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.existsByTitleIgnoreCase("Updated Book")).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book saved = invocation.getArgument(0);
            saved.setId(id);
            return saved;
        });

        BookRequestDTO updateRequest = new BookRequestDTO();
        updateRequest.setTitle("Updated Book");

        BookResponseDTO result = bookService.update(id, updateRequest);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getTitle()).isEqualTo("Updated Book");
    }

    @Test
    void update_ShouldThrow_WhenNotFound() {
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.update(id, request));
    }

    @Test
    void update_ShouldThrow_WhenDuplicateTitle() {
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.existsByTitleIgnoreCase("Test Book")).thenReturn(true);

        Book anotherBook = new Book();
        anotherBook.setId(UUID.randomUUID());
        anotherBook.setTitle("Test Book");

        when(bookRepository.findByTitleIgnoreCase("Test Book"))
                .thenReturn(Optional.of(anotherBook));

        assertThrows(AlreadyExistException.class, () -> bookService.update(id, request));
    }

    @Test
    void delete_ShouldDelete_WhenFound() {
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        bookService.delete(id);

        verify(bookRepository).delete(book);
    }

    @Test
    void delete_ShouldThrow_WhenNotFound() {
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.delete(id));
    }

    @Test
    void findById_ShouldReturn_WhenFound() {
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        BookResponseDTO result = bookService.findById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getTitle()).isEqualTo("Test Book");
    }

    @Test
    void findById_ShouldThrow_WhenNotFound() {
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.findById(id));
    }

    @Test
    void findAll_ShouldReturnList() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookResponseDTO> result = bookService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(id);
    }
}