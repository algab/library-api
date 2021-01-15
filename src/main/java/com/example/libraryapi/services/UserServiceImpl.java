package com.example.libraryapi.services;

import com.example.libraryapi.dto.UserDTO;
import com.example.libraryapi.dto.UserFormDTO;
import com.example.libraryapi.entities.User;
import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.repositories.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (!repository.existsByEmail(body.getEmail())) {
            User user = repository.save(mapper.map(body, User.class));
            return mapper.map(user, UserDTO.class);
        } else {
            throw new BusinessException(409,"CONFLICT", "E-mail is conflict");
        }
    }

    @Override
    public List<UserDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO search(Long id) throws BusinessException {
        User user = repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "User not found"));
        return mapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO update(Long id, UserFormDTO body) {
        User user = repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "User not found"));
        user.setName(body.getName());
        if (body.getEmail().equals(user.getEmail())) {
            User userUpdated = repository.save(user);
            return mapper.map(userUpdated, UserDTO.class);
        } else {
            if (!repository.existsByEmail(body.getEmail())) {
                user.setEmail(body.getEmail());
                User userUpdated = repository.save(user);
                return mapper.map(userUpdated, UserDTO.class);
            } else {
                throw new BusinessException(409,"CONFLICT", "E-mail is conflict");
            }
        }
    }

    @Override
    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "NOT_FOUND", "User not found"));
        repository.delete(user);
    }
}
