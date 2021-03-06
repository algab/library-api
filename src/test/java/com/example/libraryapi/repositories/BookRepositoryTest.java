package com.example.libraryapi.repositories;

import com.example.libraryapi.builder.AuthorBuilder;
import com.example.libraryapi.builder.BookBuilder;
import com.example.libraryapi.constants.Gender;
import com.example.libraryapi.entities.Author;
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
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@DisplayName("Tests for Book Repository")
public class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Test
    @DisplayName("Save Book Successful")
    void saveBook() {
        Book book = new Book();
        book.setTitle("A Guerra dos Tronos");
        book.setAuthors(Arrays.asList(AuthorBuilder.getAuthor()));
        book.setIsbn("1000");

        Boolean existsIsbn = this.repository.existsByIsbn(book.getIsbn());

        Assertions.assertThat(existsIsbn).isEqualTo(false);

        Book bookSaved = this.repository.save(book);

        Assertions.assertThat(bookSaved).isNotNull();
        Assertions.assertThat(bookSaved.getId()).isNotNull();
        Assertions.assertThat(bookSaved.getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(bookSaved.getIsbn()).isEqualTo(book.getIsbn());
        Assertions.assertThat(bookSaved.getAuthors().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Duplicate ISBN Book")
    void duplicateISBNBook() {
        Book book = new Book();
        book.setTitle("A Guerra dos Tronos");
        book.setAuthors(Arrays.asList(AuthorBuilder.getAuthor()));
        book.setIsbn("1000");

        this.repository.save(book);

        Boolean existsIsbn = this.repository.existsByIsbn(book.getIsbn());

        Assertions.assertThat(existsIsbn).isEqualTo(true);
    }

    @Test
    @DisplayName("List of all books")
    void listBooks() {
        Book book = new Book();
        book.setTitle("A Guerra dos Tronos");
        book.setAuthors(Arrays.asList(AuthorBuilder.getAuthor()));
        book.setIsbn("1000");

        this.repository.save(book);
        List<Book> books = this.repository.findAll();

        Assertions.assertThat(books).isNotEmpty();
        Assertions.assertThat(books.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Search Book")
    void searchBook() {
        Book book = new Book();
        book.setTitle("A Guerra dos Tronos");
        book.setAuthors(Arrays.asList(AuthorBuilder.getAuthor()));
        book.setIsbn("1000");

        Book bookSaved = this.repository.save(book);
        Optional<Book> search = this.repository.findById(bookSaved.getId());

        Assertions.assertThat(search.get()).isNotNull();
        Assertions.assertThat(search.get().getId()).isEqualTo(bookSaved.getId());
        Assertions.assertThat(search.get().getTitle()).isEqualTo(bookSaved.getTitle());
        Assertions.assertThat(bookSaved.getIsbn()).isEqualTo(book.getIsbn());
        Assertions.assertThat(bookSaved.getAuthors().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Find Loans Book")
    void findLoans() {
        Book book = this.createLoan();
        Page<Loan> loans = this.repository.findLoans(book.getId(), PageRequest.of(0 ,10));

        Assertions.assertThat(loans.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(loans.getPageable().getPageSize()).isEqualTo(10);
        Assertions.assertThat(loans.getPageable().getPageNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("List authors by book")
    void findAuthors() {
        this.createAuthor();
        Book bookSaved = this.repository.save(BookBuilder.getBook());
        Page<Author> authors = this.repository.findByAuthors(bookSaved.getId(), PageRequest.of(0, 10));

        Assertions.assertThat(authors.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(authors.getPageable().getPageSize()).isEqualTo(10);
        Assertions.assertThat(authors.getPageable().getPageNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("Update Book Successful")
    void updateBook() {
        List<Author> authors = new ArrayList<>();
        Author author = new Author();
        author.setName("George R. R. Martin");
        author.setSexo(Gender.MASCULINO);
        author.setBirthdate(LocalDate.of(1948, Month.SEPTEMBER, 20));
        authors.add(author);

        Book book = new Book();
        book.setTitle("A Guerra dos Tronos");
        book.setAuthors(authors);
        book.setIsbn("1000");

        Book bookSaved = this.repository.save(book);
        bookSaved.setTitle("A Fúria dos Reis");
        Book bookUpdated = this.repository.save(bookSaved);

        Assertions.assertThat(bookUpdated).isNotNull();
        Assertions.assertThat(bookUpdated.getId()).isNotNull();
        Assertions.assertThat(bookUpdated.getTitle()).isEqualTo(bookSaved.getTitle());
        Assertions.assertThat(bookUpdated.getIsbn()).isEqualTo(bookSaved.getIsbn());
        Assertions.assertThat(bookUpdated.getAuthors().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Delete Book Successful")
    void deleteBook() {
        Book book = new Book();
        book.setTitle("A Guerra dos Tronos");
        book.setAuthors(Arrays.asList(AuthorBuilder.getAuthor()));
        book.setIsbn("1000");

        Book bookSaved = this.repository.save(book);
        this.repository.delete(bookSaved);
        Optional<Book> bookOptional = this.repository.findById(bookSaved.getId());

        Assertions.assertThat(bookOptional).isEmpty();
    }

    public void createAuthor() {
        Author author = new Author();
        author.setName("George R. R. Martin");
        author.setSexo(Gender.MASCULINO);
        author.setBirthdate(LocalDate.of(1948, Month.SEPTEMBER, 20));
        this.authorRepository.save(author);
    }

    public Book createLoan() {
        Book book = new Book();
        book.setTitle("A Guerra dos Tronos");
        book.setAuthors(Arrays.asList(AuthorBuilder.getAuthor()));
        book.setIsbn("1000");

        User user = new User();
        user.setName("Test");
        user.setEmail("test@email.com");
        user.setSexo(Gender.MASCULINO);

        Book bookSaved = this.repository.save(book);
        User userSaved = this.userRepository.save(user);

        Loan loan = new Loan();
        loan.setBook(bookSaved);
        loan.setUser(userSaved);
        loan.setReturned(false);
        loan.setDate(LocalDate.now());

        this.loanRepository.save(loan);

        return bookSaved;
    }
}
