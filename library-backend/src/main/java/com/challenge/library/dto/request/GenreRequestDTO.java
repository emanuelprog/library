package com.challenge.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenreRequestDTO {

    @NotBlank(message = "Description is required")
    private String description;
}