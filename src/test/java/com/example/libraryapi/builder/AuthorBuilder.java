package com.example.libraryapi.builder;

import com.example.libraryapi.constants.Gender;
import com.example.libraryapi.entities.Author;

import java.time.LocalDate;
import java.time.Month;

public final class AuthorBuilder {
    public static Author getAuthor() {
        Author author = new Author();
        author.setName("George R. R. Martin");
        author.setSexo(Gender.MASCULINO);
        author.setBirthdate(LocalDate.of(1948, Month.SEPTEMBER, 20));
        return author;
    }
}
