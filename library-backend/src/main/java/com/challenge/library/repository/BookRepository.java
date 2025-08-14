package com.challenge.library.repository;

import java.util.Optional;
import java.util.UUID;

import com.challenge.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    boolean existsByTitleIgnoreCase(String title);

    Optional<Book> findByTitleIgnoreCase(String title);
}