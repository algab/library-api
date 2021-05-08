package com.example.libraryapi.services;

import com.example.libraryapi.dto.AuthorDTO;
import com.example.libraryapi.dto.AuthorFormDTO;
import com.example.libraryapi.dto.BookDTO;
import com.example.libraryapi.entities.Author;
import com.example.libraryapi.entities.Book;
import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.repositories.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public AuthorDTO save(AuthorFormDTO body) {
        Author author = this.repository.save(mapper.map(body, Author.class));
        return mapper.map(author, AuthorDTO.class);
    }

    @Override
    public Page<AuthorDTO> findAll(Pageable page) {
        Page<Author> authors = this.repository.findAll(page);
        List<AuthorDTO> listAuthors = authors.getContent()
                .stream()
                .map(author -> mapper.map(author, AuthorDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<AuthorDTO>(listAuthors, page, authors.getTotalElements());
    }

    @Override
    public AuthorDTO search(Long id) {
        Author author = this.repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "Author not found"));
        return mapper.map(author, AuthorDTO.class);
    }

    @Override
    public Page<BookDTO> findBooks(Long id, Pageable page) {
        Page<Book> books = this.repository.findByBooks(id, page);
        List<BookDTO> booksDTO = books.getContent()
                .stream()
                .map(book -> mapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<BookDTO>(booksDTO, page, books.getTotalElements());
    }

    @Override
    public AuthorDTO update(Long id, AuthorFormDTO body) {
        Author author = this.repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "Author not found"));
        author.setName(body.getName());
        author.setSexo(body.getSexo());
        author.setBirthdate(body.getBirthdate());
        Author updated = this.repository.save(author);
        return mapper.map(updated, AuthorDTO.class);
    }

    @Override
    public void delete(Long id) {
        try {
            Author author = this.repository.findById(id)
                    .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "Author not found"));
            this.repository.delete(author);
        } catch (Exception ex) {
            throw new BusinessException(409, "CONFLICT", "Deletes all books by that author");
        }
    }

}
