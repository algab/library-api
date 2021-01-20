package com.example.libraryapi.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanBookDTO {
    private Long id;
    private LocalDate date;
    private Boolean returned;
    private BookDTO book;
}
