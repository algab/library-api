package com.example.libraryapi.dto;

import com.example.libraryapi.constants.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class AuthorFormDTO {

    @NotEmpty(message = "name is required")
    private String name;

    @NotNull(message = "sexo is required")
    private Gender sexo;

    @NotNull(message = "birthdate is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthdate;

}
