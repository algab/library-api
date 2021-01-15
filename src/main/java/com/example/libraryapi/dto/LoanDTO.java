package com.example.libraryapi.dto;

import java.time.LocalDate;

public class LoanDTO {
    private Long id;
    private LocalDate date;
    private Boolean returned;
    private BookDTO book;
    private UserDTO user;
}
