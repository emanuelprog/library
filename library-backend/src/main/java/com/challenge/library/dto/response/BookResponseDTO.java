package com.challenge.library.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class BookResponseDTO {

    private UUID id;
    private String title;
    private Instant createdAt;
    private AuthorResponseDTO author;
    private GenreResponseDTO genre;
}
