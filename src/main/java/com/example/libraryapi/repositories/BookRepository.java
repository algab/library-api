package com.example.libraryapi.repositories;

import com.example.libraryapi.entities.Author;
import com.example.libraryapi.entities.Book;
import com.example.libraryapi.entities.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    Boolean existsByIsbn(String isbn);

    @Query("select loan from Loan loan where loan.book.id = ?1")
    Page<Loan> findLoans(Long id, Pageable page);

    @Query("select author from Author author join author.books book where book.id = ?1")
    Page<Author> findByAuthors(Long id, Pageable page);

}
