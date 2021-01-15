package com.example.libraryapi.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanDTO {
    private Long id;
    private LocalDate date;
    private Boolean returned;
    private BookDTO book;
    private UserDTO user;
}
