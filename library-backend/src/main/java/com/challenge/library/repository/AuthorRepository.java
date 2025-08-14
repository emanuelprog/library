package com.challenge.library.repository;

import com.challenge.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {
    boolean existsByNameIgnoreCase(String name);

    Optional<Author> findByNameIgnoreCase(String name);
}
