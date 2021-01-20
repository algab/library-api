package com.example.libraryapi.controllers;

import com.example.libraryapi.dto.LoanBookDTO;
import com.example.libraryapi.dto.UserDTO;
import com.example.libraryapi.dto.UserFormDTO;
import com.example.libraryapi.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody @Valid UserFormDTO body) {
        UserDTO user = this.service.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> users = this.service.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDTO> search(@PathVariable Long id) {
        UserDTO user = this.service.search(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping(path = "/{id}/loans")
    public ResponseEntity<List<LoanBookDTO>> findLoans(@PathVariable Long id) {
        List<LoanBookDTO> loans = this.service.findLoans(id);
        return ResponseEntity.ok(loans);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody @Valid UserFormDTO body) {
        UserDTO user = this.service.update(id, body);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.service.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
