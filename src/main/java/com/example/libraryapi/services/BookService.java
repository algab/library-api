package com.example.libraryapi.services;

import com.example.libraryapi.dto.BookDTO;
import com.example.libraryapi.dto.BookFormDTO;
import com.example.libraryapi.dto.LoanUserDTO;

import java.util.List;

public interface BookService {
    BookDTO save(BookFormDTO body);
    List<BookDTO> findAll();
    BookDTO search(Long id);
    List<LoanUserDTO> findLoans(Long id);
    BookDTO update(Long id, BookFormDTO body);
    void delete(Long id);
}
