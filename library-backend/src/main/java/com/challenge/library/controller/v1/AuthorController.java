package com.challenge.library.controller.v1;

import com.challenge.library.dto.request.AuthorRequestDTO;
import com.challenge.library.service.AuthorService;
import com.challenge.library.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Create a new author")
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody AuthorRequestDTO dto) {
        return ResponseUtil.generateResponse(authorService.create(dto), HttpStatus.CREATED, "Author created successfully");
    }

    @Operation(summary = "Update an existing author")
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody AuthorRequestDTO dto) {
        return ResponseUtil.generateResponse(authorService.update(id, dto), HttpStatus.OK, "Author updated successfully");
    }

    @Operation(summary = "Delete an author by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        authorService.delete(id);
        return ResponseUtil.generateResponse(null, HttpStatus.NO_CONTENT, "Author deleted successfully");
    }

    @Operation(summary = "Get an author by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable UUID id) {
        return ResponseUtil.generateResponse(authorService.findById(id), HttpStatus.OK, "Author retrieved successfully");
    }

    @Operation(summary = "Get all authors")
    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseUtil.generateResponse(authorService.findAll(), HttpStatus.OK, "Authors retrieved successfully");
    }
}