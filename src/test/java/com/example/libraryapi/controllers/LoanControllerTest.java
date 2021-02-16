package com.example.libraryapi.controllers;

import com.example.libraryapi.builder.LoanBuilder;
import com.example.libraryapi.dto.LoanDTO;
import com.example.libraryapi.dto.LoanFormDTO;
import com.example.libraryapi.services.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = LoanController.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Loan Controller")
public class LoanControllerTest {
    private static final String API_LOANS = "/loans";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService service;

    @Test
    @DisplayName("Save loan controller")
    public void saveLoan() throws Exception {
        LoanDTO loan = LoanBuilder.getLoanDTO();
        BDDMockito.given(this.service.save(any(LoanFormDTO.class))).willReturn(loan);
        String json = new ObjectMapper().writeValueAsString(LoanBuilder.getLoanFormDTO());

        RequestBuilder request = MockMvcRequestBuilders.post(API_LOANS).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isCreated());
        mockMvc.perform(request).andExpect(jsonPath("id").value(loan.getId()));
        mockMvc.perform(request).andExpect(jsonPath("returned").value(loan.getReturned()));
    }

    @Test
    @DisplayName("Save loan controller validate fields")
    public void saveLoan_ValidateFields() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new LoanFormDTO());

        RequestBuilder request = MockMvcRequestBuilders.post(API_LOANS).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deliver loan controller")
    public void deliverLoan() throws Exception {
        LoanDTO loan = LoanBuilder.getLoanDTO();
        loan.setReturned(true);
        BDDMockito.given(this.service.deliver(anyLong())).willReturn(loan);

        RequestBuilder request = MockMvcRequestBuilders.put(API_LOANS.concat("/" + 1 + "/deliver"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(jsonPath("id").value(loan.getId()));
        mockMvc.perform(request).andExpect(jsonPath("returned").value(true));
    }

    @Test
    @DisplayName("Delete Loan Controller")
    public void deleteLoan() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete(API_LOANS.concat("/" + 1));

        mockMvc.perform(request).andExpect(status().isNoContent());
    }
}
