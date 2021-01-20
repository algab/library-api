package com.example.libraryapi.services;

import com.example.libraryapi.dto.BookDTO;
import com.example.libraryapi.dto.BookFormDTO;
import com.example.libraryapi.dto.LoanUserDTO;
import com.example.libraryapi.entities.Book;
import com.example.libraryapi.entities.Loan;
import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.repositories.BookRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public BookDTO save(BookFormDTO body) {
        if (!repository.existsByIsbn(body.getIsbn())) {
            Book book = repository.save(mapper.map(body, Book.class));
            return mapper.map(book, BookDTO.class);
        } else {
            throw new BusinessException(409, "CONFLICT", "ISBN is conflict");
        }
    }

    @Override
    public Page<BookDTO> findAll(Pageable page) {
        Page<Book> books = this.repository.findAll(page);
        List<BookDTO> listBooks = books.getContent()
                .stream()
                .map(book -> mapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<BookDTO>(listBooks, page, books.getTotalElements());
    }

    @Override
    public BookDTO search(Long id) throws BusinessException {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "Book not found"));
        return mapper.map(book, BookDTO.class);
    }

    @Override
    public Page<LoanUserDTO> findLoans(Long id, Pageable page) {
        Page<Loan> loans = this.repository.findLoanUser(id, page);
        List<LoanUserDTO> loansUsers = loans.getContent()
                .stream()
                .map(loan -> mapper.map(loan, LoanUserDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<LoanUserDTO>(loansUsers, page, loans.getTotalElements());
    }

    @Override
    public BookDTO update(Long id, BookFormDTO body) throws BusinessException {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "Book not found"));
        book.setTitle(body.getTitle());
        book.setAuthor(body.getAuthor());
        if (body.getIsbn().equals(book.getIsbn())) {
            Book bookUpdated = repository.save(book);
            return mapper.map(bookUpdated, BookDTO.class);
        } else {
            if (!repository.existsByIsbn(body.getIsbn())) {
                book.setIsbn(body.getIsbn());
                Book bookUpdated = repository.save(book);
                return mapper.map(bookUpdated, BookDTO.class);
            } else {
                throw new BusinessException(409, "CONFLICT", "ISBN is conflict");
            }
        }
    }

    @Override
    public void delete(Long id) throws BusinessException {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "Book not found"));
        repository.delete(book);
    }
}
