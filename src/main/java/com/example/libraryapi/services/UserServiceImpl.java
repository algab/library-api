package com.example.libraryapi.services;

import com.example.libraryapi.dto.LoanBookDTO;
import com.example.libraryapi.dto.UserDTO;
import com.example.libraryapi.dto.UserFormDTO;
import com.example.libraryapi.entities.Loan;
import com.example.libraryapi.entities.User;
import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.repositories.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDTO save(UserFormDTO body) {
        if (!this.repository.existsByEmail(body.getEmail())) {
            User user = this.repository.save(mapper.map(body, User.class));
            return mapper.map(user, UserDTO.class);
        } else {
            throw new BusinessException(409,"CONFLICT", "E-mail is conflict");
        }
    }

    @Override
    public Page<UserDTO> findAll(Pageable page) {
        Page<User> users = this.repository.findAll(page);
        List<UserDTO> listUsers = users.getContent()
                .stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<UserDTO>(listUsers, page, users.getTotalElements());
    }

    @Override
    public UserDTO search(Long id) throws BusinessException {
        User user = this.repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "User not found"));
        return mapper.map(user, UserDTO.class);
    }

    @Override
    public Page<LoanBookDTO> findLoans(Long id, Pageable page) {
        Page<Loan> loans = this.repository.findLoans(id, page);
        List<LoanBookDTO> loansBooks = loans.getContent()
                .stream()
                .map(loan -> mapper.map(loan, LoanBookDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<LoanBookDTO>(loansBooks, page, loans.getTotalElements());
    }

    @Override
    public UserDTO update(Long id, UserFormDTO body) {
        User user = this.repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "User not found"));
        user.setName(body.getName());
        user.setSexo(body.getSexo());
        if (body.getEmail().equals(user.getEmail())) {
            User userUpdated = this.repository.save(user);
            return mapper.map(userUpdated, UserDTO.class);
        } else {
            if (!this.repository.existsByEmail(body.getEmail())) {
                user.setEmail(body.getEmail());
                User userUpdated = this.repository.save(user);
                return mapper.map(userUpdated, UserDTO.class);
            } else {
                throw new BusinessException(409,"CONFLICT", "E-mail is conflict");
            }
        }
    }

    @Override
    public void delete(Long id) {
        User user = this.repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "User not found"));
        this.repository.delete(user);
    }

}
