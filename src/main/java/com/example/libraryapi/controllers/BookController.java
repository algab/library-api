package com.example.libraryapi.controllers;

import com.example.libraryapi.dto.BookDTO;
import com.example.libraryapi.dto.BookFormDTO;
import com.example.libraryapi.dto.LoanUserDTO;
import com.example.libraryapi.services.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService service;

    @PostMapping
    public ResponseEntity<BookDTO> save(@RequestBody @Valid BookFormDTO body) {
        BookDTO book = this.service.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> findAll() {
        List<BookDTO> books = this.service.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BookDTO> search(@PathVariable Long id) {
        BookDTO book = this.service.search(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping(path = "/{id}/loans")
    public ResponseEntity<List<LoanUserDTO>> findLoans(@PathVariable Long id) {
        List<LoanUserDTO> loans = this.service.findLoans(id);
        return ResponseEntity.ok(loans);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<BookDTO> search(@PathVariable Long id, @RequestBody @Valid BookFormDTO body) {
        BookDTO book = this.service.update(id, body);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.service.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
