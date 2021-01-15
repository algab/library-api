package com.example.libraryapi.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BookFormDTO {
    @NotEmpty(message = "title is required")
    private String title;

    @NotEmpty(message = "author is required")
    private String author;

    @NotEmpty(message = "isbn is required")
    private String isbn;
}
