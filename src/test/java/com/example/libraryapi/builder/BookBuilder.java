package com.example.libraryapi.builder;

import com.example.libraryapi.dto.BookDTO;
import com.example.libraryapi.dto.BookFormDTO;
import com.example.libraryapi.dto.LoanUserDTO;
import com.example.libraryapi.entities.Author;
import com.example.libraryapi.entities.Book;
import com.example.libraryapi.entities.Loan;
import com.example.libraryapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BookBuilder {
    public static Book getBook() {
        List<Author> authors = new ArrayList<>();
        authors.add(AuthorBuilder.getAuthor());

        Book book = new Book();
        book.setId(1L);
        book.setTitle("A Guerra dos Tronos");
        book.setIsbn("1000");
        book.setAuthors(authors);
        return book;
    }

    public static BookDTO getBookDTO() {
        BookDTO book = new BookDTO();
        book.setId(1L);
        book.setTitle("A Guerra dos Tronos");
        book.setIsbn("1000");
        return book;
    }

    public static BookFormDTO getBookFormDTO() {
        BookFormDTO bookForm = new BookFormDTO();
        bookForm.setIsbn("1010");
        bookForm.setTitle("A Guerra dos Tronos");
        bookForm.setIsbn("1000");
        bookForm.setAuthors(Arrays.asList(1L));
        return bookForm;
    }

    public static Page<BookDTO> listBooks() {
        BookDTO book = new BookDTO();
        book.setId(1L);
        book.setTitle("A Guerra dos Tronos");
        book.setIsbn("1000");

        List<BookDTO> books = new ArrayList<>();
        books.add(book);

        return new PageImpl<>(books, PageRequest.of(0, 1), 1);
    }

    public static Page<LoanUserDTO> getLoansBook() {
        LoanUserDTO loan = new LoanUserDTO();
        loan.setId(1L);
        loan.setReturned(false);
        loan.setDate(LocalDate.now());
        loan.setUser(UserBuilder.getUserDTO());

        List<LoanUserDTO> loans = new ArrayList<>();
        loans.add(loan);

        return new PageImpl<>(loans, PageRequest.of(0, 1), 1);
    }

    public static Page<Loan> loansBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("A Guerra dos Tronos");
        book.setAuthors(Arrays.asList(AuthorBuilder.getAuthor()));
        book.setIsbn("1000");

        User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("test@email.com");

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setBook(book);
        loan.setUser(user);
        loan.setReturned(false);
        loan.setDate(LocalDate.now());

        List<Loan> loans = new ArrayList<>();
        loans.add(loan);

        return new PageImpl<>(loans, PageRequest.of(0, 10), 1);
    }

    public static Page<Author> authors() {
        return new PageImpl<>(Arrays.asList(AuthorBuilder.getAuthor()), PageRequest.of(0, 10), 1);
    }
}
