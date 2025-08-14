package com.challenge.library.service.impl;

import com.challenge.library.dto.request.AuthorRequestDTO;
import com.challenge.library.dto.response.AuthorResponseDTO;
import com.challenge.library.entity.Author;
import com.challenge.library.exception.AlreadyExistException;
import com.challenge.library.exception.NotFoundException;
import com.challenge.library.mapper.AuthorMapper;
import com.challenge.library.repository.AuthorRepository;
import com.challenge.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorResponseDTO create(AuthorRequestDTO dto) {
        validateDuplicateName(dto.getName(), null);

        Author author = authorMapper.toEntity(dto);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    public AuthorResponseDTO update(UUID id, AuthorRequestDTO dto) {
        authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Autor não encontrado."));

        validateDuplicateName(dto.getName(), id);

        Author author = authorMapper.toEntity(dto);
        author.setId(id);

        return authorMapper.toResponse(authorRepository.save(author));
    }

    public void delete(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Autor não encontrado."));
        authorRepository.delete(author);
    }

    public AuthorResponseDTO findById(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Autor não encontrado."));
        return authorMapper.toResponse(author);
    }

    public List<AuthorResponseDTO> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toResponse)
                .toList();
    }

    private void validateDuplicateName(String name, UUID ignoreId) {
        boolean exists = authorRepository.existsByNameIgnoreCase(name.trim());

        if (exists && ignoreId != null) {
            exists = authorRepository.findByNameIgnoreCase(name.trim())
                    .filter(a -> !a.getId().equals(ignoreId))
                    .isPresent();
        }

        if (exists) {
            throw new AlreadyExistException("Já existe um autor com esse nome.");
        }
    }
}