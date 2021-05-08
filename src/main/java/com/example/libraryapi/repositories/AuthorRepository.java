package com.example.libraryapi.repositories;

import com.example.libraryapi.entities.Author;
import com.example.libraryapi.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("select book from Book book join book.authors author where author.id = ?1")
    Page<Book> findByBooks(Long id, Pageable page);

}
