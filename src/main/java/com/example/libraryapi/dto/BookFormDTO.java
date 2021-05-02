package com.example.libraryapi.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class BookFormDTO {
    @NotEmpty(message = "title is required")
    private String title;

    @NotEmpty(message = "isbn is required")
    private String isbn;

    @NotEmpty(message = "author is required")
    private List<Long> authors;
}
