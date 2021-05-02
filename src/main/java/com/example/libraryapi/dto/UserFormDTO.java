package com.example.libraryapi.dto;

import com.example.libraryapi.constants.Gender;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserFormDTO {

    @NotEmpty(message = "name is required")
    private String name;

    @Email(message = "email is required")
    @NotEmpty(message = "email is required")
    private String email;

    @NotNull(message = "sexo is required")
    private Gender sexo;

}
