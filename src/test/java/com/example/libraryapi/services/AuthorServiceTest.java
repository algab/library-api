package com.example.libraryapi.services;

import com.example.libraryapi.builder.AuthorBuilder;
import com.example.libraryapi.builder.BookBuilder;
import com.example.libraryapi.dto.AuthorDTO;
import com.example.libraryapi.dto.AuthorFormDTO;
import com.example.libraryapi.dto.BookDTO;
import com.example.libraryapi.entities.Author;
import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.repositories.AuthorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Author Service")
public class AuthorServiceTest {

    @Autowired
    private AuthorService service;

    @MockBean
    private AuthorRepository repository;

    @Test
    @DisplayName("Save author")
    public void saveAuthor() {
        Author author = AuthorBuilder.getAuthor();

        when(this.repository.save(any(Author.class))).thenReturn(author);

        AuthorDTO authorDTO = this.service.save(AuthorBuilder.getAuthorFormDTO());

        assertThat(authorDTO.getId()).isNotNull();
        assertThat(authorDTO.getName()).isEqualTo(author.getName());
        assertThat(authorDTO.getSexo()).isEqualTo(author.getSexo());
        assertThat(authorDTO.getBirthdate()).isEqualTo(author.getBirthdate());
    }

    @Test
    @DisplayName("List all author")
    public void listAuthors() {
        Author author = AuthorBuilder.getAuthor();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Author> authors = Arrays.asList(author);
        Page<Author> page = new PageImpl<>(authors, pageRequest, 1);

        when(this.repository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<AuthorDTO> pageAuthorDTO = this.service.findAll(pageRequest);

        assertThat(pageAuthorDTO.getContent()).hasSize(1);
        assertThat(pageAuthorDTO.getTotalPages()).isEqualTo(1);
        assertThat(pageAuthorDTO.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Search author")
    public void searchAuthor() {
        Author author = AuthorBuilder.getAuthor();

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(author));

        AuthorDTO authorDTO = this.service.search(author.getId());

        assertThat(authorDTO.getId()).isNotNull();
        assertThat(authorDTO.getName()).isEqualTo(author.getName());
        assertThat(authorDTO.getSexo()).isEqualTo(author.getSexo());
        assertThat(authorDTO.getBirthdate()).isEqualTo(author.getBirthdate());
    }

    @Test
    @DisplayName("Search author not found")
    public void searchAuthor_NotFound() {
        Author author = AuthorBuilder.getAuthor();

        when(this.repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.search(author.getId()));
    }

    @Test
    @DisplayName("Find books")
    public void findBooks() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(this.repository.findByBooks(anyLong(), any(PageRequest.class)))
                .thenReturn(AuthorBuilder.books());

        Page<BookDTO> books = this.service.findBooks(1L, pageRequest);

        assertThat(books).isNotNull();
        assertThat(books.getContent()).hasSize(1);
        assertThat(books.getTotalPages()).isEqualTo(1);
        assertThat(books.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Update author")
    public void updateAuthor() {
        Author author = AuthorBuilder.getAuthor();
        AuthorFormDTO authorFormDTO = AuthorBuilder.getAuthorFormDTO();
        authorFormDTO.setName("Author Test");

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(author));
        when(this.repository.save(any(Author.class))).thenReturn(author);

        AuthorDTO authorDTO = this.service.update(author.getId(), authorFormDTO);

        assertThat(authorDTO.getId()).isNotNull();
        assertThat(authorDTO.getName()).isEqualTo(authorFormDTO.getName());
        assertThat(authorDTO.getSexo()).isEqualTo(authorFormDTO.getSexo());
        assertThat(authorDTO.getBirthdate()).isEqualTo(authorFormDTO.getBirthdate());
    }

    @Test
    @DisplayName("Update author not found")
    public void updateAuthor_NotFound() {
        Author author = AuthorBuilder.getAuthor();

        when(this.repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.update(author.getId(), AuthorBuilder.getAuthorFormDTO()));
    }

    @Test
    @DisplayName("Delete author")
    public void deleteAuthor() {
        Author author = AuthorBuilder.getAuthor();

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(author));

        this.service.delete(1L);

        verify(this.repository, times(1)).delete(author);
    }

    @Test
    @DisplayName("Delete author conflict")
    public void deleteAuthor_ConflictBook() {
        Author author = AuthorBuilder.getAuthor();

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(author));
        doThrow(new BusinessException()).when(this.repository).delete(any(Author.class));

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.delete(1L));
    }

    @Test
    @DisplayName("Delete author not found")
    public void deleteAuthor_NotFound() {
        when(this.repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.delete(1L));
    }
}
