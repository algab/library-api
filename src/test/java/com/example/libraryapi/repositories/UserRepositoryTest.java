package com.example.libraryapi.repositories;

import com.example.libraryapi.entities.Book;
import com.example.libraryapi.entities.Loan;
import com.example.libraryapi.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for User Repository")
public class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Test
    @DisplayName("Save User Successful")
    void saveUser() {
        User user = new User();
        user.setName("Teste");
        user.setEmail("teste@email.com");

        Boolean existsEmail = this.repository.existsByEmail(user.getEmail());

        Assertions.assertThat(existsEmail).isEqualTo(false);

        User userSaved = this.repository.save(user);

        Assertions.assertThat(userSaved).isNotNull();
        Assertions.assertThat(userSaved.getId()).isNotNull();
        Assertions.assertThat(userSaved.getName()).isEqualTo(user.getName());
        Assertions.assertThat(userSaved.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Duplicate Email User")
    void duplicateUser() {
        User user = new User();
        user.setName("Teste");
        user.setEmail("teste@email.com");

        this.repository.save(user);
        Boolean existsEmail = this.repository.existsByEmail(user.getEmail());

        Assertions.assertThat(existsEmail).isEqualTo(true);
    }

    @Test
    @DisplayName("List of all users")
    void listBooks() {
        User user = new User();
        user.setName("Teste");
        user.setEmail("teste@email.com");

        this.repository.save(user);
        List<User> users = this.repository.findAll();

        Assertions.assertThat(users).isNotEmpty();
        Assertions.assertThat(users.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Search User")
    void searchBook() {
        User user = new User();
        user.setName("Teste");
        user.setEmail("teste@email.com");

        User userSaved = this.repository.save(user);
        Optional<User> search = this.repository.findById(userSaved.getId());

        Assertions.assertThat(search.get()).isNotNull();
        Assertions.assertThat(search.get().getId()).isEqualTo(userSaved.getId());
        Assertions.assertThat(search.get().getName()).isEqualTo(userSaved.getName());
        Assertions.assertThat(search.get().getEmail()).isEqualTo(userSaved.getEmail());
    }

    @Test
    @DisplayName("Update User Successful")
    void updateBook() {
        User user = new User();
        user.setName("Teste");
        user.setEmail("teste@email.com");

        User userSaved = this.repository.save(user);
        userSaved.setName("Teste Teste");
        User userUpdated = this.repository.save(userSaved);

        Assertions.assertThat(userUpdated).isNotNull();
        Assertions.assertThat(userUpdated.getId()).isNotNull();
        Assertions.assertThat(userUpdated.getName()).isEqualTo(userSaved.getName());
        Assertions.assertThat(userUpdated.getEmail()).isEqualTo(userSaved.getEmail());
    }

    @Test
    @DisplayName("Delete User Successful")
    void deleteBook() {
        User user = new User();
        user.setName("Teste");
        user.setEmail("teste@email.com");

        User userSaved = this.repository.save(user);
        this.repository.delete(userSaved);
        Optional<User> userOptional = this.repository.findById(userSaved.getId());

        Assertions.assertThat(userOptional).isEmpty();
    }

    @Test
    @DisplayName("Find Loans User")
    void findLoans() {
        User user = this.createLoan();
        Page<Loan> loans = this.repository.findLoans(user.getId(), PageRequest.of(0 ,10));

        Assertions.assertThat(loans.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(loans.getPageable().getPageSize()).isEqualTo(10);
        Assertions.assertThat(loans.getPageable().getPageNumber()).isEqualTo(0);
    }

    public User createLoan() {
        Book book = new Book();
        book.setTitle("Livro Teste");
        //book.setAuthor("Teste");
        book.setIsbn("1010");

        User user = new User();
        user.setName("Teste");
        user.setEmail("teste@email.com");

        Book bookSaved = this.bookRepository.save(book);
        User userSaved = this.repository.save(user);

        Loan loan = new Loan();
        loan.setBook(bookSaved);
        loan.setUser(userSaved);
        loan.setReturned(false);
        loan.setDate(LocalDate.now());

        this.loanRepository.save(loan);

        return userSaved;
    }
}
