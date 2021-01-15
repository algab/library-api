package com.example.libraryapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResponseException {
    private Integer status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
}
