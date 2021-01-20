package com.example.libraryapi.repositories;

import com.example.libraryapi.entities.Book;
import com.example.libraryapi.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Boolean existsByIsbn(String isbn);
    @Query("select loan from Loan loan where loan.book.id = ?1")
    List<Loan> findLoanUser(Long id);
}
