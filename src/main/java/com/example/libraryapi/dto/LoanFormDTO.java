package com.example.libraryapi.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoanFormDTO {
    @NotNull(message = "idBook is required")
    private Integer idBook;

    @NotNull(message = "idUser is required")
    private Integer idUser;
}
