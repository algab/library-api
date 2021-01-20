package com.example.libraryapi.services;

import com.example.libraryapi.dto.LoanBookDTO;
import com.example.libraryapi.dto.UserDTO;
import com.example.libraryapi.dto.UserFormDTO;

import java.util.List;

public interface UserService {
    UserDTO save(UserFormDTO body);
    List<UserDTO> findAll();
    UserDTO search(Long id);
    List<LoanBookDTO> findLoans(Long id);
    UserDTO update(Long id, UserFormDTO body);
    void delete(Long id);
}
