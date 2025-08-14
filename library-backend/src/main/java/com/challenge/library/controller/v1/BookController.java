package com.challenge.library.controller.v1;

import com.challenge.library.dto.request.BookRequestDTO;
import com.challenge.library.service.BookService;
import com.challenge.library.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Create a new book")
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody BookRequestDTO dto) {
        return ResponseUtil.generateResponse(bookService.create(dto), HttpStatus.CREATED, "Book created successfully");
    }

    @Operation(summary = "Update an existing book")
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody BookRequestDTO dto) {
        return ResponseUtil.generateResponse(bookService.update(id, dto), HttpStatus.OK, "Book updated successfully");
    }

    @Operation(summary = "Delete a book by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        bookService.delete(id);
        return ResponseUtil.generateResponse(null, HttpStatus.NO_CONTENT, "Book deleted successfully");
    }

    @Operation(summary = "Get a book by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable UUID id) {
        return ResponseUtil.generateResponse(bookService.findById(id), HttpStatus.OK, "Book retrieved successfully");
    }

    @Operation(summary = "Get all books")
    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseUtil.generateResponse(bookService.findAll(), HttpStatus.OK, "Books retrieved successfully");
    }
}