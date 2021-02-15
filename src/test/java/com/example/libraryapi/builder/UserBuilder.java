package com.example.libraryapi.builder;

import com.example.libraryapi.dto.UserFormDTO;
import com.example.libraryapi.entities.Book;
import com.example.libraryapi.entities.Loan;
import com.example.libraryapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class UserBuilder {
    public static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Autor Teste");
        user.setEmail("teste@email.com");
        return user;
    }

    public static UserFormDTO getUserFormDTO() {
        UserFormDTO userForm = new UserFormDTO();
        userForm.setName("Autor Teste");
        userForm.setEmail("teste@email.com");
        return userForm;
    }

    public static Page<Loan> loansUser() {
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
