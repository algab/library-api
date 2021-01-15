package com.example.libraryapi.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserFormDTO {
    @NotEmpty(message = "name is required")
    private String name;

    @Email(message = "email is required")
    @NotEmpty(message = "email is required")
    private String email;
}
