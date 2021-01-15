package com.example.libraryapi.services;

import com.example.libraryapi.dto.LoanDTO;
import com.example.libraryapi.dto.LoanFormDTO;

public interface LoanService {
    LoanDTO save(LoanFormDTO body);
    LoanDTO deliver(Long id);
    void delete(Long id);
}
