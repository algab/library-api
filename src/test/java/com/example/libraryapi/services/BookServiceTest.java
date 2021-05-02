package com.example.libraryapi.services;

import com.example.libraryapi.builder.BookBuilder;
import com.example.libraryapi.dto.BookDTO;
import com.example.libraryapi.dto.BookFormDTO;
import com.example.libraryapi.dto.LoanUserDTO;
import com.example.libraryapi.entities.Book;
import com.example.libraryapi.entities.Loan;
import com.example.libraryapi.entities.User;
import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.repositories.BookRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.libraryapi.repositories.LoanRepository;
import com.example.libraryapi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Book Service")
public class BookServiceTest {
    @Autowired
    private BookService service;

    @MockBean
    private BookRepository repository;

    @Test
    @DisplayName("Save book")
    public void saveBook() {
        Book book = BookBuilder.getBook();

        when(this.repository.existsByIsbn(anyString())).thenReturn(false);
        when(this.repository.save(any(Book.class))).thenReturn(book);

        BookDTO bookDTO = this.service.save(BookBuilder.getBookFormDTO());

        assertThat(bookDTO.getId()).isNotNull();
        assertThat(bookDTO.getTitle()).isEqualTo(book.getTitle());
        //assertThat(bookDTO.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    @DisplayName("Book ISBN conflict")
    public void bookIsbnConflict() {
        when(this.repository.existsByIsbn(anyString())).thenReturn(true);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.save(BookBuilder.getBookFormDTO()));
    }

    @Test
    @DisplayName("List all books")
    public void listBooks() {
        Book book = BookBuilder.getBook();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Book> books = Arrays.asList(book);
        Page<Book> page = new PageImpl<>(books, pageRequest, 1);

        when(this.repository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<BookDTO> pageBookDTO = this.service.findAll(pageRequest);

        assertThat(pageBookDTO.getContent()).hasSize(1);
        assertThat(pageBookDTO.getTotalPages()).isEqualTo(1);
        assertThat(pageBookDTO.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Search book")
    public void searchBook() {
        Book book = BookBuilder.getBook();

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(book));

        BookDTO bookDTO = this.service.search(book.getId());

        assertThat(bookDTO).isNotNull();
        assertThat(bookDTO.getIsbn()).isEqualTo(book.getIsbn());
        assertThat(bookDTO.getTitle()).isEqualTo(book.getTitle());
        //assertThat(bookDTO.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    @DisplayName("Search book not found")
    public void searchBook_NotFound() {
        Book book = BookBuilder.getBook();

        when(this.repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.search(book.getId()));
    }

    @Test
    @DisplayName("Find loans book")
    public void findLoansBook() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(this.repository.findLoans(anyLong(), any(PageRequest.class)))
                .thenReturn(BookBuilder.loansBook());

        Page<LoanUserDTO> loanUsers = this.service.findLoans(1L, pageRequest);

        assertThat(loanUsers).isNotNull();
        assertThat(loanUsers.getContent()).hasSize(1);
        assertThat(loanUsers.getTotalPages()).isEqualTo(1);
        assertThat(loanUsers.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Update book")
    public void updateBook() {
        Book book = BookBuilder.getBook();
        BookFormDTO bookFormDTO = BookBuilder.getBookFormDTO();
        //bookFormDTO.setAuthor("Teste 10");
        bookFormDTO.setTitle("Teste 10");

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(book));
        when(this.repository.save(any(Book.class))).thenReturn(book);

        BookDTO bookDTO = this.service.update(book.getId(), bookFormDTO);

        assertThat(bookDTO.getId()).isNotNull();
        assertThat(bookDTO.getIsbn()).isEqualTo(bookFormDTO.getIsbn());
        assertThat(bookDTO.getTitle()).isEqualTo(bookFormDTO.getTitle());
        //assertThat(bookDTO.getAuthor()).isEqualTo(bookFormDTO.getAuthor());
    }

    @Test
    @DisplayName("Update book with new ISBN")
    public void updateBook_NewISBN() {
        Book book = BookBuilder.getBook();
        BookFormDTO bookFormDTO = BookBuilder.getBookFormDTO();
        bookFormDTO.setIsbn("2020");
        //bookFormDTO.setAuthor("Teste 10");
        bookFormDTO.setTitle("Teste 10");

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(book));
        when(this.repository.existsByIsbn(anyString())).thenReturn(false);
        when(this.repository.save(any(Book.class))).thenReturn(book);

        BookDTO bookDTO = this.service.update(book.getId(), bookFormDTO);

        assertThat(bookDTO.getId()).isNotNull();
        assertThat(bookDTO.getIsbn()).isEqualTo(bookFormDTO.getIsbn());
        assertThat(bookDTO.getTitle()).isEqualTo(bookFormDTO.getTitle());
        //assertThat(bookDTO.getAuthor()).isEqualTo(bookFormDTO.getAuthor());
    }

    @Test
    @DisplayName("Update book not found")
    public void updateBook_NotFound() {
        Book book = BookBuilder.getBook();

        when(this.repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.update(book.getId(), BookBuilder.getBookFormDTO()));
    }

    @Test
    @DisplayName("Update book with new ISBN conflict")
    public void updateBook_ISBNConflict() {
        Book book = BookBuilder.getBook();
        BookFormDTO bookFormDTO = BookBuilder.getBookFormDTO();
        bookFormDTO.setIsbn("2020");
        //bookFormDTO.setAuthor("Teste 10");
        bookFormDTO.setTitle("Teste 10");

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(book));
        when(this.repository.existsByIsbn(anyString())).thenReturn(true);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.update(book.getId(), bookFormDTO));
    }

    @Test
    @DisplayName("Delete book")
    public void deleteBook() {
        Book book = BookBuilder.getBook();

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(book));

        this.service.delete(1L);

        verify(this.repository, times(1)).delete(book);
    }

    @Test
    @DisplayName("Delete book not found")
    public void deleteBook_NotFound() {
        when(this.repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.delete(1L));
    }
}
