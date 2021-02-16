package com.example.libraryapi.builder;

import com.example.libraryapi.dto.BookDTO;
import com.example.libraryapi.dto.BookFormDTO;
import com.example.libraryapi.entities.Book;
import com.example.libraryapi.entities.Loan;
import com.example.libraryapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class BookBuilder {
    public static Book getBook() {
        Book book = new Book();
        book.setId(1L);
        book.setIsbn("1010");
        book.setTitle("Livro Teste");
        book.setAuthor("Teste");
        return book;
    }

    public static BookDTO getBookDTO() {
        BookDTO book = new BookDTO();
        book.setId(1L);
        book.setIsbn("1010");
        book.setTitle("Livro Teste");
        book.setAuthor("Teste");
        return book;
    }

    public static BookFormDTO getBookFormDTO() {
        BookFormDTO bookForm = new BookFormDTO();
        bookForm.setIsbn("1010");
        bookForm.setTitle("Livro Teste");
        bookForm.setAuthor("Teste");
        return bookForm;
    }

    public static Page<BookDTO> listBooks() {
        BookDTO book = new BookDTO();
        book.setId(1L);
        book.setIsbn("1010");
        book.setTitle("Livro Teste");
        book.setAuthor("Teste");

        List<BookDTO> books = new ArrayList<>();
        books.add(book);

        return new PageImpl<>(books, PageRequest.of(0, 1), 1);
    }

    public static Page<Loan> loansBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Livro Teste");
        book.setAuthor("Teste");
        book.setIsbn("1010");

        User user = new User();
        user.setId(1L);
        user.setName("Teste");
        user.setEmail("teste@email.com");

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
}
