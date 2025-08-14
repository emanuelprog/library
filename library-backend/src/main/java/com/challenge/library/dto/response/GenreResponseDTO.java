package com.challenge.library.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class GenreResponseDTO {

    private UUID id;
    private String description;
    private Instant createdAt;
}
