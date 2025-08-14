package com.challenge.library.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "genre", schema = "library")
@Data
public class Genre {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String description;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
