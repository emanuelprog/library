package com.challenge.library.repository;

import com.challenge.library.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, UUID> {
    boolean existsByDescriptionIgnoreCase(String description);
    Optional<Genre> findByDescriptionIgnoreCase(String description);
}
