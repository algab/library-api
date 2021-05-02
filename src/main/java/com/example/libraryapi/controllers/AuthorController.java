package com.example.libraryapi.controllers;

import com.example.libraryapi.dto.AuthorDTO;
import com.example.libraryapi.dto.AuthorFormDTO;
import com.example.libraryapi.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService service;

    @PostMapping
    public ResponseEntity<AuthorDTO> save(@RequestBody @Valid AuthorFormDTO body) {
        AuthorDTO author = this.service.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(author);
    }

    @GetMapping
    public ResponseEntity<Page<AuthorDTO>> findAll(
            @PageableDefault(page=0, size=10, sort="id", direction = Sort.Direction.ASC) Pageable page
    ) {
        Page<AuthorDTO> authors = this.service.findAll(page);
        return ResponseEntity.ok(authors);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AuthorDTO> search(@PathVariable Long id) {
        AuthorDTO author = this.service.search(id);
        return ResponseEntity.ok(author);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<AuthorDTO> update(@PathVariable Long id, @RequestBody @Valid AuthorFormDTO body) {
        AuthorDTO author = this.service.update(id, body);
        return ResponseEntity.ok(author);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.service.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
