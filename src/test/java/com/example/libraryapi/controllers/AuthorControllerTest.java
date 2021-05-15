package com.example.libraryapi.controllers;

import com.example.libraryapi.builder.AuthorBuilder;
import com.example.libraryapi.dto.*;
import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.services.AuthorService;
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

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = AuthorController.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Author Controller")
public class AuthorControllerTest {
    private static final String API_AUTHORS = "/authors";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService service;

    @Test
    @DisplayName("Save author controller")
    public void saveAuthor() throws Exception {
        AuthorDTO author = AuthorBuilder.getAuthorDTO();
        BDDMockito.given(this.service.save(any(AuthorFormDTO.class))).willReturn(author);
        String json = readFileAsString("src/test/resources/json/authorForm.json");

        RequestBuilder request = MockMvcRequestBuilders.post(API_AUTHORS).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isCreated());
        mockMvc.perform(request).andExpect(jsonPath("id").value(author.getId()));
        mockMvc.perform(request).andExpect(jsonPath("name").value(author.getName()));
    }

    @Test
    @DisplayName("Save author controller validate fields")
    public void saveAuthor_ValidateFields() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new AuthorFormDTO());

        RequestBuilder request = MockMvcRequestBuilders.post(API_AUTHORS).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Save author controller validate no body")
    public void saveAuthor_NoBody() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post(API_AUTHORS)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("List Authors controller")
    public void listAuthors() throws Exception {
        Page<AuthorDTO> page = AuthorBuilder.listAuthors();
        BDDMockito.given(this.service.findAll(any(PageRequest.class))).willReturn(page);

        RequestBuilder request = MockMvcRequestBuilders.get(API_AUTHORS)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(jsonPath("content").isArray());
        mockMvc.perform(request).andExpect(jsonPath("size").value(1));
        mockMvc.perform(request).andExpect(jsonPath("totalPages").value(1));
    }

    @Test
    @DisplayName("Search author controller")
    public void searchAuthor() throws Exception {
        AuthorDTO author = AuthorBuilder.getAuthorDTO();

        BDDMockito.given(this.service.search(anyLong())).willReturn(author);

        RequestBuilder request = MockMvcRequestBuilders.get(API_AUTHORS.concat("/" + 1))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(jsonPath("id").value(author.getId()));
        mockMvc.perform(request).andExpect(jsonPath("name").value(author.getName()));
    }

    @Test
    @DisplayName("Find books by author controller")
    public void findBooks() throws Exception {
        Page<BookDTO> books = AuthorBuilder.booksDTO();

        BDDMockito.given(this.service.findBooks(anyLong(), any(PageRequest.class))).willReturn(books);

        RequestBuilder request = MockMvcRequestBuilders.get(String.format("%s/%d/books", API_AUTHORS, 1))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(jsonPath("content").isArray());
        mockMvc.perform(request).andExpect(jsonPath("size").value(10));
        mockMvc.perform(request).andExpect(jsonPath("totalPages").value(1));
    }

    @Test
    @DisplayName("Update author controller")
    public void updateAuthor() throws Exception {
        AuthorDTO author = AuthorBuilder.getAuthorDTO();
        BDDMockito.given(this.service.update(anyLong(),any(AuthorFormDTO.class))).willReturn(author);
        String json = readFileAsString("src/test/resources/json/authorForm.json");

        RequestBuilder request = MockMvcRequestBuilders.put(API_AUTHORS.concat("/" + 1)).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(jsonPath("id").value(author.getId()));
        mockMvc.perform(request).andExpect(jsonPath("name").value(author.getName()));
    }

    @Test
    @DisplayName("Delete author controller")
    public void deleteAuthor() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete(API_AUTHORS.concat("/" + 1));

        mockMvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete author conflict controller")
    public void deleteAuthor_Conflict() throws Exception {
        BDDMockito.doThrow(new BusinessException(409, "CONFLICT", "Deletes all books by that author")).when(this.service).delete(anyLong());

        RequestBuilder request = MockMvcRequestBuilders.delete(API_AUTHORS.concat("/" + 1));

        mockMvc.perform(request).andExpect(status().isConflict());
    }

    public static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}
