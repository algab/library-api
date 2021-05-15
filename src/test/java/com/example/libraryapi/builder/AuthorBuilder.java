package com.example.libraryapi.builder;

import com.example.libraryapi.constants.Gender;
import com.example.libraryapi.dto.AuthorDTO;
import com.example.libraryapi.dto.AuthorFormDTO;
import com.example.libraryapi.dto.BookDTO;
import com.example.libraryapi.entities.Author;
import com.example.libraryapi.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AuthorBuilder {
    public static Author getAuthor() {
        Author author = new Author();
        author.setId(1L);
        author.setName("George R. R. Martin");
        author.setSexo(Gender.MASCULINO);
        author.setBirthdate(LocalDate.of(1948, Month.SEPTEMBER, 20));
        return author;
    }

    public static AuthorDTO getAuthorDTO() {
        AuthorDTO author = new AuthorDTO();
        author.setId(1L);
        author.setName("George R. R. Martin");
        author.setSexo(Gender.MASCULINO);
        author.setBirthdate(LocalDate.of(1948, Month.SEPTEMBER, 20));
        return author;
    }

    public static AuthorFormDTO getAuthorFormDTO() {
        AuthorFormDTO author = new AuthorFormDTO();
        author.setName("George R. R. Martin");
        author.setSexo(Gender.MASCULINO);
        author.setBirthdate(LocalDate.of(1948, Month.SEPTEMBER, 20));
        return author;
    }

    public static Page<AuthorDTO> listAuthors() {
        AuthorDTO author = new AuthorDTO();
        author.setId(1L);
        author.setName("George R. R. Martin");
        author.setSexo(Gender.MASCULINO);
        author.setBirthdate(LocalDate.of(1948, Month.SEPTEMBER, 20));

        List<AuthorDTO> authors = new ArrayList<>();
        authors.add(author);

        return new PageImpl<>(authors, PageRequest.of(0, 1), 1);
    }

    public static Page<Book> books() {
        return new PageImpl<>(Arrays.asList(BookBuilder.getBook()), PageRequest.of(0, 10), 1);
    }

    public static Page<BookDTO> booksDTO() {
        return new PageImpl<>(Arrays.asList(BookBuilder.getBookDTO()), PageRequest.of(0, 10), 1);
    }
}
