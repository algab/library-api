package com.example.libraryapi.services;

import com.example.libraryapi.dto.BookDTO;
import com.example.libraryapi.dto.BookFormDTO;
import com.example.libraryapi.dto.LoanUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDTO save(BookFormDTO body);
    Page<BookDTO> findAll(Pageable page);
    BookDTO search(Long id);
    Page<LoanUserDTO> findLoans(Long id, Pageable page);
    BookDTO update(Long id, BookFormDTO body);
    void delete(Long id);
}
