package com.example.libraryapi.builder;

import com.example.libraryapi.constants.Gender;
import com.example.libraryapi.dto.LoanBookDTO;
import com.example.libraryapi.dto.UserDTO;
import com.example.libraryapi.dto.UserFormDTO;
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

public final class UserBuilder {
    public static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setName("User Test");
        user.setEmail("teste@email.com");
        user.setSexo(Gender.MASCULINO);
        return user;
    }

    public static UserDTO getUserDTO() {
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setName("User Test");
        user.setEmail("teste@email.com");
        user.setSexo(Gender.MASCULINO);
        return user;
    }

    public static UserFormDTO getUserFormDTO() {
        UserFormDTO userForm = new UserFormDTO();
        userForm.setName("User Test");
        userForm.setEmail("teste@email.com");
        userForm.setSexo(Gender.MASCULINO);
        return userForm;
    }

    public static Page<UserDTO> listUsers() {
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setName("User Test");
        user.setEmail("teste@email.com");
        user.setSexo(Gender.MASCULINO);

        List<UserDTO> users = new ArrayList<>();
        users.add(user);

        return new PageImpl<>(users, PageRequest.of(0, 1), 1);
    }

    public static Page<LoanBookDTO> getLoansUser() {
        LoanBookDTO loan = new LoanBookDTO();
        loan.setId(1L);
        loan.setReturned(false);
        loan.setDate(LocalDate.now());
        loan.setBook(BookBuilder.getBookDTO());

        List<LoanBookDTO> loans = new ArrayList<>();
        loans.add(loan);

        return new PageImpl<>(loans, PageRequest.of(0, 1), 1);
    }

    public static Page<Loan> loansUser() {
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
}
