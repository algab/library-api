package com.example.libraryapi.controllers;

import com.example.libraryapi.dto.LoanDTO;
import com.example.libraryapi.dto.LoanFormDTO;
import com.example.libraryapi.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/loans")
public class LoanController {
    @Autowired
    private LoanService service;

    @PostMapping
    public ResponseEntity<LoanDTO> save(@RequestBody @Valid LoanFormDTO body) {
        LoanDTO loan = this.service.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @PutMapping(path = "/{id}/deliver")
    public ResponseEntity<LoanDTO> deliver(@PathVariable Long id) {
        LoanDTO loan = this.service.deliver(id);
        return ResponseEntity.ok(loan);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.service.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
