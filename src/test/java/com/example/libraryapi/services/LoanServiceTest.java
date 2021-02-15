package com.example.libraryapi.services;

import com.example.libraryapi.builder.BookBuilder;
import com.example.libraryapi.builder.LoanBuilder;
import com.example.libraryapi.builder.UserBuilder;
import com.example.libraryapi.dto.LoanDTO;
import com.example.libraryapi.entities.Book;
import com.example.libraryapi.entities.Loan;
import com.example.libraryapi.entities.User;
import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.repositories.BookRepository;
import com.example.libraryapi.repositories.LoanRepository;
import com.example.libraryapi.repositories.UserRepository;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Loan Service")
public class LoanServiceTest {
    @Autowired
    private LoanService service;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private LoanRepository loanRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Save Loan")
    public void saveLoan() {
        Book book = BookBuilder.getBook();
        User user = UserBuilder.getUser();
        Loan loan = LoanBuilder.getLoan();

        when(this.bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(this.userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(this.loanRepository.save(any(Loan.class))).thenReturn(loan);

        LoanDTO loanDTO = this.service.save(LoanBuilder.getLoanFormDTO());

        assertThat(loanDTO.getId()).isNotNull();
        assertThat(loanDTO.getBook().getIsbn()).isEqualTo(book.getIsbn());
        assertThat(loanDTO.getUser().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Save loan book not found")
    public void saveLoan_BookNotFound() {
        when(this.bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.save(LoanBuilder.getLoanFormDTO()));
    }

    @Test
    @DisplayName("Save loan user not found")
    public void saveLoan_UserNotFound() {
        Book book = BookBuilder.getBook();

        when(this.bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(this.userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.save(LoanBuilder.getLoanFormDTO()));
    }

    @Test
    @DisplayName("Deliver loan")
    public void deliverLoan() {
        when(this.loanRepository.findById(anyLong())).thenReturn(Optional.of(LoanBuilder.getLoan()));

        Loan loan = LoanBuilder.getLoan();
        loan.setReturned(true);

        when(this.loanRepository.save(any(Loan.class))).thenReturn(loan);

        LoanDTO loanDTO = this.service.deliver(loan.getId());

        assertThat(loanDTO.getId()).isNotNull();
        assertThat(loanDTO.getReturned()).isEqualTo(true);
    }

    @Test
    @DisplayName("Deliver loan not found")
    public void deliverLoan_NotFound() {
        when(this.loanRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.deliver(1L));
    }

    @Test
    @DisplayName("Book delivered")
    public void bookDelivered() {
        Loan loan = LoanBuilder.getLoan();
        loan.setReturned(true);

        when(this.loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.deliver(1L));
    }

    @Test
    @DisplayName("Delete loan")
    public void deleteLoan() {
        Loan loan = LoanBuilder.getLoan();

        when(this.loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));

        this.service.delete(1L);

        verify(this.loanRepository, times(1)).delete(loan);
    }

    @Test
    @DisplayName("Delete loan not found")
    public void deleteLoan_NotFound() {
        when(this.loanRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.delete(1L));
    }
}
