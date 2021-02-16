package com.example.libraryapi.controllers;

import com.example.libraryapi.builder.BookBuilder;
import com.example.libraryapi.dto.BookDTO;
import com.example.libraryapi.dto.BookFormDTO;
import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = BookController.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Book Controller")
public class BookControllerTest {
    private static final String API_BOOKS = "/books";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService service;

    @Test
    @DisplayName("Save book controller")
    public void saveBook() throws Exception {
        BookDTO book = BookBuilder.getBookDTO();
        BDDMockito.given(this.service.save(any(BookFormDTO.class))).willReturn(book);
        String json = new ObjectMapper().writeValueAsString(BookBuilder.getBookFormDTO());

        RequestBuilder request = MockMvcRequestBuilders.post(API_BOOKS).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isCreated());
        mockMvc.perform(request).andExpect(jsonPath("id").value(book.getId()));
        mockMvc.perform(request).andExpect(jsonPath("isbn").value(book.getIsbn()));
        mockMvc.perform(request).andExpect(jsonPath("title").value(book.getTitle()));
        mockMvc.perform(request).andExpect(jsonPath("author").value(book.getAuthor()));
    }

    @Test
    @DisplayName("Save book controller ISBN conflict")
    public void saveBook_ISBNConflict() throws Exception {
        BDDMockito.given(this.service.save(any(BookFormDTO.class)))
                .willThrow(new BusinessException(409, "CONFLICT", "ISBN is conflict"));
        String json = new ObjectMapper().writeValueAsString(BookBuilder.getBookFormDTO());

        RequestBuilder request = MockMvcRequestBuilders.post(API_BOOKS).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Save book controller validate fields")
    public void saveBook_ValidateFields() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new BookFormDTO());

        RequestBuilder request = MockMvcRequestBuilders.post(API_BOOKS).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("List Books Controller")
    public void listBooks() throws Exception {
        Page<BookDTO> page = BookBuilder.listBooks();
        BDDMockito.given(this.service.findAll(any(PageRequest.class))).willReturn(page);

        RequestBuilder request = MockMvcRequestBuilders.get(API_BOOKS)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(jsonPath("content").isArray());
        mockMvc.perform(request).andExpect(jsonPath("size").value(1));
        mockMvc.perform(request).andExpect(jsonPath("totalPages").value(1));
    }

    @Test
    @DisplayName("Search Book Controller")
    public void searchBook() throws Exception {
        BookDTO book = BookBuilder.getBookDTO();

        BDDMockito.given(this.service.search(anyLong())).willReturn(book);

        RequestBuilder request = MockMvcRequestBuilders.get(API_BOOKS.concat("/" + 1))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(jsonPath("id").value(book.getId()));
        mockMvc.perform(request).andExpect(jsonPath("isbn").value(book.getIsbn()));
        mockMvc.perform(request).andExpect(jsonPath("title").value(book.getTitle()));
        mockMvc.perform(request).andExpect(jsonPath("author").value(book.getAuthor()));
    }

    @Test
    @DisplayName("Update Book Controller")
    public void updateBook() throws Exception {
        BookDTO book = BookBuilder.getBookDTO();
        BookFormDTO bookForm = BookBuilder.getBookFormDTO();
        book.setAuthor("Novo Teste");
        bookForm.setAuthor("Novo Teste");
        BDDMockito.given(this.service.update(anyLong(),any(BookFormDTO.class))).willReturn(book);
        String json = new ObjectMapper().writeValueAsString(bookForm);

        RequestBuilder request = MockMvcRequestBuilders.put(API_BOOKS.concat("/" + 1)).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(jsonPath("id").value(book.getId()));
        mockMvc.perform(request).andExpect(jsonPath("isbn").value(book.getIsbn()));
        mockMvc.perform(request).andExpect(jsonPath("title").value(book.getTitle()));
        mockMvc.perform(request).andExpect(jsonPath("author").value(book.getAuthor()));
    }

    @Test
    @DisplayName("Delete Book Controller")
    public void deleteBook() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete(API_BOOKS.concat("/" + 1));

        mockMvc.perform(request).andExpect(status().isNoContent());
    }
}
