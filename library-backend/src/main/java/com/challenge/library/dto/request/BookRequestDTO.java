package com.challenge.library.dto.request;

import com.challenge.library.dto.response.AuthorResponseDTO;
import com.challenge.library.dto.response.GenreResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Author is required")
    private AuthorResponseDTO author;

    @NotNull(message = "Genre is required")
    private GenreResponseDTO genre;
}