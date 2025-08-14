package com.challenge.library.service.impl;

import com.challenge.library.dto.request.BookRequestDTO;
import com.challenge.library.dto.response.BookResponseDTO;
import com.challenge.library.entity.Book;
import com.challenge.library.exception.NotFoundException;
import com.challenge.library.mapper.BookMapper;
import com.challenge.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private UUID id;
    private Book book;
    private BookRequestDTO request;
    private BookResponseDTO response;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        book = new Book();
        book.setId(id);
        request = new BookRequestDTO();
        response = new BookResponseDTO();
        response.setId(id);
    }

    @Test
    void create_ShouldReturnResponse() {
        when(bookMapper.toEntity(request)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toResponse(book)).thenReturn(response);

        BookResponseDTO result = bookService.create(request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void update_ShouldThrow_WhenNotFound() {
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookService.update(id, request));
    }

    @Test
    void update_ShouldReturn_WhenFound() {
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toEntity(request)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toResponse(book)).thenReturn(response);

        BookResponseDTO result = bookService.update(id, request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void delete_ShouldThrow_WhenNotFound() {
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookService.delete(id));
    }

    @Test
    void delete_ShouldDelete_WhenFound() {
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        bookService.delete(id);
        verify(bookRepository).delete(book);
    }

    @Test
    void findById_ShouldThrow_WhenNotFound() {
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookService.findById(id));
    }

    @Test
    void findById_ShouldReturn_WhenFound() {
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toResponse(book)).thenReturn(response);

        BookResponseDTO result = bookService.findById(id);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void findAll_ShouldReturnList() {
        when(bookRepository.findAll()).thenReturn(List.of(book));
        when(bookMapper.toResponse(book)).thenReturn(response);

        List<BookResponseDTO> result = bookService.findAll();

        assertThat(result).containsExactly(response);
    }
}