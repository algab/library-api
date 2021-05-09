package com.example.libraryapi.repositories;

import com.example.libraryapi.builder.AuthorBuilder;
import com.example.libraryapi.entities.Author;
import com.example.libraryapi.entities.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Author Repository")
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Save Author Successful")
    void saveAuthor() {
        Author authorSaved = this.repository.save(AuthorBuilder.getAuthor());

        Assertions.assertThat(authorSaved).isNotNull();
        Assertions.assertThat(authorSaved.getId()).isNotNull();
        Assertions.assertThat(authorSaved.getName()).isEqualTo(AuthorBuilder.getAuthor().getName());
        Assertions.assertThat(authorSaved.getSexo()).isEqualTo(AuthorBuilder.getAuthor().getSexo());
        Assertions.assertThat(authorSaved.getBirthdate()).isEqualTo(AuthorBuilder.getAuthor().getBirthdate());
    }

    @Test
    @DisplayName("List of all authors")
    void listAuthors() {
        this.repository.save(AuthorBuilder.getAuthor());
        List<Author> authors = this.repository.findAll();

        Assertions.assertThat(authors).isNotEmpty();
        Assertions.assertThat(authors.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Search Author")
    void searchAuthor() {
        Author authorSaved = this.repository.save(AuthorBuilder.getAuthor());
        Optional<Author> search = this.repository.findById(authorSaved.getId());

        Assertions.assertThat(search).isNotNull();
        Assertions.assertThat(search.get().getId()).isNotNull();
        Assertions.assertThat(search.get().getName()).isEqualTo(AuthorBuilder.getAuthor().getName());
        Assertions.assertThat(search.get().getSexo()).isEqualTo(AuthorBuilder.getAuthor().getSexo());
        Assertions.assertThat(search.get().getBirthdate()).isEqualTo(AuthorBuilder.getAuthor().getBirthdate());
    }

    @Test
    @DisplayName("List books by author")
    void findBooks() {
        Author authorSaved = this.repository.save(AuthorBuilder.getAuthor());
        this.createBook(authorSaved);
        Page<Book> books = this.repository.findByBooks(authorSaved.getId(), PageRequest.of(0, 10));

        Assertions.assertThat(books.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(books.getPageable().getPageSize()).isEqualTo(10);
        Assertions.assertThat(books.getPageable().getPageNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("Update Author Successful")
    void updateAuthor() {
        Author authorSaved = this.repository.save(AuthorBuilder.getAuthor());
        authorSaved.setName("Author Test");
        Author authorUpdated = this.repository.save(authorSaved);

        Assertions.assertThat(authorUpdated).isNotNull();
        Assertions.assertThat(authorUpdated.getId()).isNotNull();
        Assertions.assertThat(authorUpdated.getName()).isEqualTo(authorSaved.getName());
        Assertions.assertThat(authorUpdated.getSexo()).isEqualTo(authorSaved.getSexo());
        Assertions.assertThat(authorUpdated.getBirthdate()).isEqualTo(authorSaved.getBirthdate());
    }

    @Test
    @DisplayName("Delete Author Successful")
    void deleteAuthor() {
        Author authorSaved = this.repository.save(AuthorBuilder.getAuthor());
        this.repository.delete(authorSaved);
        Optional<Author> authorOptional = this.repository.findById(authorSaved.getId());

        Assertions.assertThat(authorOptional).isEmpty();
    }

    @Test
    @DisplayName("Delete Author Conflict")
    void deleteAuthorConflict() {
        Author authorSaved = this.repository.save(AuthorBuilder.getAuthor());
        this.createBook(authorSaved);
        Optional<Author> authorOptional = this.repository.findById(authorSaved.getId());

        Assertions.assertThat(authorOptional).isNotEmpty();
    }

    public void createBook(Author author) {
        Book book = new Book();
        book.setTitle("A Guerra dos Tronos");
        book.setIsbn("1000");
        book.setAuthors(Arrays.asList(author));

        this.bookRepository.save(book);
    }

}
