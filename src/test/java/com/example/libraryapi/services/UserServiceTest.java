package com.example.libraryapi.services;

import com.example.libraryapi.builder.UserBuilder;
import com.example.libraryapi.dto.*;
import com.example.libraryapi.entities.User;
import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Tests for User Service")
public class UserServiceTest {
    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

    @Test
    @DisplayName("Save User")
    public void saveUser() {
        User user = UserBuilder.getUser();

        when(this.repository.existsByEmail(anyString())).thenReturn(false);
        when(this.repository.save(any(User.class))).thenReturn(user);

        UserDTO userDTO = this.service.save(UserBuilder.getUserFormDTO());

        assertThat(userDTO.getId()).isNotNull();
        assertThat(userDTO.getName()).isEqualTo(user.getName());
        assertThat(userDTO.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("User Email conflict")
    public void userEmailConflict() {
        when(this.repository.existsByEmail(anyString())).thenReturn(true);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.save(UserBuilder.getUserFormDTO()));
    }

    @Test
    @DisplayName("List all users")
    public void listUsers() {
        User user = UserBuilder.getUser();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<User> users = Arrays.asList(user);
        Page<User> page = new PageImpl<>(users, pageRequest, 1);

        when(this.repository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<UserDTO> pageUserDTO = this.service.findAll(pageRequest);

        assertThat(pageUserDTO.getContent()).hasSize(1);
        assertThat(pageUserDTO.getTotalPages()).isEqualTo(1);
        assertThat(pageUserDTO.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Search User")
    public void searchUser() {
        User user = UserBuilder.getUser();

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDTO userDTO = this.service.search(user.getId());

        assertThat(userDTO.getId()).isNotNull();
        assertThat(userDTO.getName()).isEqualTo(user.getName());
        assertThat(userDTO.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Search user not found")
    public void searchUser_NotFound() {
        when(this.repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.search(1L));
    }

    @Test
    @DisplayName("Find loans user")
    public void findLoansUser() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(this.repository.findLoans(anyLong(), any(PageRequest.class)))
                .thenReturn(UserBuilder.loansUser());

        Page<LoanBookDTO> loanUsers = this.service.findLoans(1L, pageRequest);

        assertThat(loanUsers).isNotNull();
        assertThat(loanUsers.getContent()).hasSize(1);
        assertThat(loanUsers.getTotalPages()).isEqualTo(1);
        assertThat(loanUsers.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Update user")
    public void updateUser() {
        User user = UserBuilder.getUser();
        UserFormDTO userFormDTO = UserBuilder.getUserFormDTO();
        userFormDTO.setName("Novo Teste");
        userFormDTO.setEmail("teste@email.com");

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(this.repository.save(any(User.class))).thenReturn(user);

        UserDTO userDTO = this.service.update(user.getId(), userFormDTO);

        assertThat(userDTO.getId()).isNotNull();
        assertThat(userDTO.getName()).isEqualTo(userFormDTO.getName());
        assertThat(userDTO.getEmail()).isEqualTo(userFormDTO.getEmail());
    }

    @Test
    @DisplayName("Update user with new email")
    public void updateUser_NewEmail() {
        User user = UserBuilder.getUser();
        UserFormDTO userFormDTO = UserBuilder.getUserFormDTO();
        userFormDTO.setName("Novo Teste");
        userFormDTO.setEmail("novo@email.com");

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(this.repository.existsByEmail(anyString())).thenReturn(false);
        when(this.repository.save(any(User.class))).thenReturn(user);

        UserDTO userDTO = this.service.update(user.getId(), userFormDTO);

        assertThat(userDTO.getId()).isNotNull();
        assertThat(userDTO.getName()).isEqualTo(userFormDTO.getName());
        assertThat(userDTO.getEmail()).isEqualTo(userFormDTO.getEmail());
    }

    @Test
    @DisplayName("Update user not found")
    public void updateUser_NotFound() {
        User user = UserBuilder.getUser();

        when(this.repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.update(user.getId(), UserBuilder.getUserFormDTO()));
    }

    @Test
    @DisplayName("Update user with new email conflict")
    public void updateBook_EmailConflict() {
        User user = UserBuilder.getUser();
        UserFormDTO userFormDTO = UserBuilder.getUserFormDTO();
        userFormDTO.setName("Novo Teste");
        userFormDTO.setEmail("novo@email.com");

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(this.repository.existsByEmail(anyString())).thenReturn(true);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.update(user.getId(), userFormDTO));
    }

    @Test
    @DisplayName("Delete user")
    public void deleteUser() {
        User user = UserBuilder.getUser();

        when(this.repository.findById(anyLong())).thenReturn(Optional.of(user));

        this.service.delete(1L);

        verify(this.repository, times(1)).delete(user);
    }

    @Test
    @DisplayName("Delete user not found")
    public void deleteUser_NotFound() {
        when(this.repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> this.service.delete(1L));
    }
}
