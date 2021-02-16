package com.example.libraryapi.controllers;

import com.example.libraryapi.builder.UserBuilder;
import com.example.libraryapi.dto.UserDTO;
import com.example.libraryapi.dto.UserFormDTO;
import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = UserController.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Tests for User Controller")
public class UserControllerTest {
    private static final String API_USERS = "/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Test
    @DisplayName("Save user controller")
    public void saveUser() throws Exception {
        UserDTO user = UserBuilder.getUserDTO();
        BDDMockito.given(this.service.save(any(UserFormDTO.class))).willReturn(user);
        String json = new ObjectMapper().writeValueAsString(UserBuilder.getUserFormDTO());

        RequestBuilder request = MockMvcRequestBuilders.post(API_USERS).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isCreated());
        mockMvc.perform(request).andExpect(jsonPath("id").value(user.getId()));
        mockMvc.perform(request).andExpect(jsonPath("name").value(user.getName()));
        mockMvc.perform(request).andExpect(jsonPath("email").value(user.getEmail()));
    }

    @Test
    @DisplayName("Save book controller email conflict")
    public void saveBook_EmailConflict() throws Exception {
        BDDMockito.given(this.service.save(any(UserFormDTO.class)))
                .willThrow(new BusinessException(409, "CONFLICT", "Email is conflict"));
        String json = new ObjectMapper().writeValueAsString(UserBuilder.getUserFormDTO());

        RequestBuilder request = MockMvcRequestBuilders.post(API_USERS).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Save user controller validate fields")
    public void saveUser_ValidateFields() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new UserFormDTO());

        RequestBuilder request = MockMvcRequestBuilders.post(API_USERS).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("List Users Controller")
    public void listUsers() throws Exception {
        Page<UserDTO> page = UserBuilder.listUsers();
        BDDMockito.given(this.service.findAll(any(PageRequest.class))).willReturn(page);

        RequestBuilder request = MockMvcRequestBuilders.get(API_USERS)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(jsonPath("content").isArray());
        mockMvc.perform(request).andExpect(jsonPath("size").value(1));
        mockMvc.perform(request).andExpect(jsonPath("totalPages").value(1));
    }

    @Test
    @DisplayName("Search User Controller")
    public void searchUser() throws Exception {
        UserDTO user = UserBuilder.getUserDTO();

        BDDMockito.given(this.service.search(anyLong())).willReturn(user);

        RequestBuilder request = MockMvcRequestBuilders.get(API_USERS.concat("/" + 1))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(jsonPath("id").value(user.getId()));
        mockMvc.perform(request).andExpect(jsonPath("name").value(user.getName()));
        mockMvc.perform(request).andExpect(jsonPath("email").value(user.getEmail()));
    }

    @Test
    @DisplayName("Update User Controller")
    public void updateUser() throws Exception {
        UserDTO user = UserBuilder.getUserDTO();
        UserFormDTO userForm = UserBuilder.getUserFormDTO();
        user.setName("Novo Teste");
        userForm.setName("Novo Teste");
        BDDMockito.given(this.service.update(anyLong(),any(UserFormDTO.class))).willReturn(user);
        String json = new ObjectMapper().writeValueAsString(userForm);

        RequestBuilder request = MockMvcRequestBuilders.put(API_USERS.concat("/" + 1)).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(jsonPath("id").value(user.getId()));
        mockMvc.perform(request).andExpect(jsonPath("name").value(user.getName()));
        mockMvc.perform(request).andExpect(jsonPath("email").value(user.getEmail()));
    }

    @Test
    @DisplayName("Delete User Controller")
    public void deleteUser() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete(API_USERS.concat("/" + 1));

        mockMvc.perform(request).andExpect(status().isNoContent());
    }
}
