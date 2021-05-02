package com.example.libraryapi.dto;

import com.example.libraryapi.constants.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthorDTO {

    private Long id;
    private String name;
    private Gender sexo;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthdate;

}
