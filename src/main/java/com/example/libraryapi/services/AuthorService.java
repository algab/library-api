package com.example.libraryapi.services;

import com.example.libraryapi.dto.AuthorDTO;
import com.example.libraryapi.dto.AuthorFormDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {

    AuthorDTO save(AuthorFormDTO body);

    Page<AuthorDTO> findAll(Pageable page);

    AuthorDTO search(Long id);

    AuthorDTO update(Long id, AuthorFormDTO body);

    void delete(Long id);

}
