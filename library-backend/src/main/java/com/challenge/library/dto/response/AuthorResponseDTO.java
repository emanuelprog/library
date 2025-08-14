package com.challenge.library.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class AuthorResponseDTO {

    private UUID id;
    private String name;
    private Instant createdAt;
}
