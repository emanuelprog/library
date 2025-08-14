package com.challenge.library.service.impl;

import com.challenge.library.dto.request.BookRequestDTO;
import com.challenge.library.dto.response.BookResponseDTO;
import com.challenge.library.entity.Book;
import com.challenge.library.exception.AlreadyExistException;
import com.challenge.library.exception.NotFoundException;
import com.challenge.library.mapper.BookMapper;
import com.challenge.library.repository.BookRepository;
import com.challenge.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookResponseDTO create(BookRequestDTO dto) {
        validateDuplicate(dto.getTitle(), null);

        Book book = bookMapper.toEntity(dto);
        return bookMapper.toResponse(bookRepository.save(book));
    }

    public BookResponseDTO update(UUID id, BookRequestDTO dto) {
        bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado."));

        validateDuplicate(dto.getTitle(), id);

        Book book = bookMapper.toEntity(dto);
        book.setId(id);

        return bookMapper.toResponse(bookRepository.save(book));
    }

    public void delete(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado."));
        bookRepository.delete(book);
    }

    public BookResponseDTO findById(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado."));
        return bookMapper.toResponse(book);
    }

    public List<BookResponseDTO> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    private void validateDuplicate(String title, UUID ignoreId) {
        boolean exists = bookRepository.existsByTitleIgnoreCase(
                title.trim()
        );

        if (exists && ignoreId != null) {
            exists = bookRepository.findByTitleIgnoreCase(
                    title.trim()
            ).filter(b -> !b.getId().equals(ignoreId)).isPresent();
        }

        if (exists) {
            throw new AlreadyExistException("Já existe um livro com esse título.");
        }
    }
}