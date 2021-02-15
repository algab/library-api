package com.example.libraryapi.builder;

import com.example.libraryapi.dto.LoanFormDTO;
import com.example.libraryapi.entities.Loan;

import java.time.LocalDate;

public final class LoanBuilder {
    public static Loan getLoan() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setReturned(false);
        loan.setDate(LocalDate.now());
        loan.setBook(BookBuilder.getBook());
        loan.setUser(UserBuilder.getUser());
        return loan;
    }

    public static LoanFormDTO getLoanFormDTO() {
        LoanFormDTO loanFormDTO = new LoanFormDTO();
        loanFormDTO.setIdBook(1);
        loanFormDTO.setIdUser(1);
        return loanFormDTO;
    }
}
