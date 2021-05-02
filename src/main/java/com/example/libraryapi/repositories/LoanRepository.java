package com.example.libraryapi.repositories;

import com.example.libraryapi.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> { }
