package com.example.libraryapi.repositories;

import com.example.libraryapi.entities.Loan;
import com.example.libraryapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    @Query("select loan from Loan loan where loan.user.id = ?1")
    Page<Loan> findLoans(Long id, Pageable page);
}
