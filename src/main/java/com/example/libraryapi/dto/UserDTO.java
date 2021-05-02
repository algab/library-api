package com.example.libraryapi.dto;

import com.example.libraryapi.constants.Gender;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String name;

    private String email;

    private Gender sexo;

}
