package com.example.libraryapi.services;

import com.example.libraryapi.dto.AuthorDTO;
import com.example.libraryapi.dto.BookDTO;
import com.example.libraryapi.dto.BookFormDTO;
import com.example.libraryapi.dto.LoanUserDTO;
import com.example.libraryapi.entities.Author;
import com.example.libraryapi.entities.Book;
import com.example.libraryapi.entities.Loan;
import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.repositories.AuthorRepository;
import com.example.libraryapi.repositories.BookRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public BookDTO save(BookFormDTO body) {
        if (!this.repository.existsByIsbn(body.getIsbn())) {
            Book bookMapper = mapper.map(body, Book.class);
            bookMapper.setAuthors(this.findAuthors(body.getAuthors()));
            Book book = this.repository.save(bookMapper);
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
        return new PageImpl<>(listBooks, page, books.getTotalElements());
    }

    @Override
    public BookDTO search(Long id) throws BusinessException {
        Book book = this.repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "Book not found"));
        return mapper.map(book, BookDTO.class);
    }

    @Override
    public Page<LoanUserDTO> findLoans(Long id, Pageable page) {
        Page<Loan> loans = this.repository.findLoans(id, page);
        List<LoanUserDTO> loansUsers = loans.getContent()
                .stream()
                .map(loan -> mapper.map(loan, LoanUserDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(loansUsers, page, loans.getTotalElements());
    }

    @Override
    public Page<AuthorDTO> findAuthors(Long id, Pageable page) {
        Page<Author> authors = this.repository.findByAuthors(id, page);
        List<AuthorDTO> authorsDTO = authors.getContent()
                .stream()
                .map(author -> mapper.map(author, AuthorDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(authorsDTO, page, authors.getTotalElements());
    }

    @Override
    public BookDTO update(Long id, BookFormDTO body) throws BusinessException {
        Book book = this.repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "Book not found"));
        book.setTitle(body.getTitle());
        book.setAuthors(this.findAuthors(body.getAuthors()));
        if (body.getIsbn().equals(book.getIsbn())) {
            Book bookUpdated = this.repository.save(book);
            return mapper.map(bookUpdated, BookDTO.class);
        } else {
            if (!this.repository.existsByIsbn(body.getIsbn())) {
                book.setIsbn(body.getIsbn());
                Book bookUpdated = this.repository.save(book);
                return mapper.map(bookUpdated, BookDTO.class);
            } else {
                throw new BusinessException(409, "CONFLICT", "ISBN is conflict");
            }
        }
    }

    @Override
    public void delete(Long id) throws BusinessException {
        Book book = this.repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "Book not found"));
        this.repository.delete(book);
    }

    private List<Author> findAuthors(List<Long> authors) {
        return authors.stream().map(id -> {
            Optional<Author> author = this.authorRepository.findById(id);
            if (author.isPresent()) {
                return author.get();
            }
            return null;
        }).collect(Collectors.toList());
    }

}
