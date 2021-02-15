package com.example.libraryapi.services;

import com.example.libraryapi.dto.LoanDTO;
import com.example.libraryapi.dto.LoanFormDTO;
import com.example.libraryapi.entities.Book;
import com.example.libraryapi.entities.Loan;
import com.example.libraryapi.entities.User;
import com.example.libraryapi.exceptions.BusinessException;
import com.example.libraryapi.repositories.BookRepository;
import com.example.libraryapi.repositories.LoanRepository;

import com.example.libraryapi.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public LoanDTO save(LoanFormDTO body) {
        Book book = this.bookRepository.findById(body.getIdBook().longValue())
                .orElseThrow(() -> new BusinessException(404,"NOT_FOUND","Book not found"));
        User user = this.userRepository.findById(body.getIdUser().longValue())
                .orElseThrow(() -> new BusinessException(404,"NOT_FOUND","User not found"));
        Loan loan = new Loan(LocalDate.now(), false, book, user);
        Loan loanSaved = this.loanRepository.save(loan);
        return mapper.map(loanSaved, LoanDTO.class);
    }

    @Override
    public LoanDTO deliver(Long id) {
        Loan loan = this.loanRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404,"NOT_FOUND","Loan not found"));
        if (!loan.getReturned()) {
            loan.setReturned(true);
            this.loanRepository.save(loan);
            return mapper.map(loan, LoanDTO.class);
        } else {
            throw new BusinessException(409, "CONFLICT", "Book already delivered");
        }
    }

    @Override
    public void delete(Long id) {
        Loan loan = this.loanRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404,"NOT_FOUND","Loan not found"));
        this.loanRepository.delete(loan);
    }
}
