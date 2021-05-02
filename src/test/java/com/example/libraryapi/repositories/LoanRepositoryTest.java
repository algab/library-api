package com.example.libraryapi.repositories;

import com.example.libraryapi.entities.Book;
import com.example.libraryapi.entities.Loan;
import com.example.libraryapi.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Loan Repository")
public class LoanRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Test
    @DisplayName("Save Book Loan")
    void saveLoan() {
        Loan loan = this.createLoan();

        Loan loanSaved = this.loanRepository.save(loan);

        Assertions.assertThat(loanSaved).isNotNull();
        Assertions.assertThat(loanSaved.getId()).isEqualTo(loanSaved.getId());
        Assertions.assertThat(loanSaved.getBook().getTitle()).isEqualTo(loan.getBook().getTitle());
        Assertions.assertThat(loanSaved.getUser().getName()).isEqualTo(loan.getUser().getName());
    }

    @Test
    @DisplayName("Find Loan")
    void findLoan() {
        Loan loan = this.createLoan();

        Loan loanSaved = this.loanRepository.save(loan);
        Optional<Loan> optional = this.loanRepository.findById(loanSaved.getId());

        Assertions.assertThat(optional.get()).isNotNull();
        Assertions.assertThat(optional.get().getId()).isEqualTo(loanSaved.getId());
        Assertions.assertThat(optional.get().getBook().getTitle()).isEqualTo(loan.getBook().getTitle());
        Assertions.assertThat(optional.get().getUser().getName()).isEqualTo(loan.getUser().getName());
    }

    @Test
    @DisplayName("Delete Loan")
    void deleteLoan() {
        Loan loanSaved = this.loanRepository.save(this.createLoan());
        this.loanRepository.delete(loanSaved);
        Optional<Loan> optional = this.loanRepository.findById(loanSaved.getId());

        Assertions.assertThat(optional).isEmpty();
    }

    public Loan createLoan() {
        Book book = new Book();
        book.setTitle("Livro Teste");
        //book.setAuthor("Teste");
        book.setIsbn("1010");

        User user = new User();
        user.setName("Teste");
        user.setEmail("teste@email.com");

        Book bookSaved = this.bookRepository.save(book);
        User userSaved = this.userRepository.save(user);

        Loan loan = new Loan();
        loan.setBook(bookSaved);
        loan.setUser(userSaved);
        loan.setReturned(false);
        loan.setDate(LocalDate.now());

        return loan;
    }
}
