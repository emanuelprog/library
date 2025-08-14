package com.challenge.library.controller.v1;

import com.challenge.library.dto.request.GenreRequestDTO;
import com.challenge.library.service.GenreService;
import com.challenge.library.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @Operation(summary = "Create a new genre")
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody GenreRequestDTO dto) {
        return ResponseUtil.generateResponse(genreService.create(dto), HttpStatus.CREATED, "Genre created successfully");
    }

    @Operation(summary = "Update an existing genre")
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody GenreRequestDTO dto) {
        return ResponseUtil.generateResponse(genreService.update(id, dto), HttpStatus.OK, "Genre updated successfully");
    }

    @Operation(summary = "Delete a genre by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        genreService.delete(id);
        return ResponseUtil.generateResponse(null, HttpStatus.NO_CONTENT, "Genre deleted successfully");
    }

    @Operation(summary = "Get a genre by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable UUID id) {
        return ResponseUtil.generateResponse(genreService.findById(id), HttpStatus.OK, "Genre retrieved successfully");
    }

    @Operation(summary = "Get all genres")
    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseUtil.generateResponse(genreService.findAll(), HttpStatus.OK, "Genres retrieved successfully");
    }
}