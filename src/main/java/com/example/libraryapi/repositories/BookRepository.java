package com.example.libraryapi.repositories;

import com.example.libraryapi.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Boolean existsByIsbn(String isbn);
}
