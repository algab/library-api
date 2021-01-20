package com.example.libraryapi.services;

import com.example.libraryapi.dto.LoanBookDTO;
import com.example.libraryapi.dto.UserDTO;
import com.example.libraryapi.dto.UserFormDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDTO save(UserFormDTO body);
    Page<UserDTO> findAll(Pageable page);
    UserDTO search(Long id);
    Page<LoanBookDTO> findLoans(Long id, Pageable page);
    UserDTO update(Long id, UserFormDTO body);
    void delete(Long id);
}
