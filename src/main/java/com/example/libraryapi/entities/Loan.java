package com.example.libraryapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "loans")
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Boolean returned;

    @ManyToOne
    @JoinColumn(name = "id_book", referencedColumnName = "id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id", nullable = false)
    private User user;

    public Loan(LocalDate date, Boolean returned, Book book, User user) {
        this.date = date;
        this.returned = returned;
        this.book = book;
        this.user = user;
    }
}
