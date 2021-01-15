package com.example.libraryapi.exceptions;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException {
    private Integer status;
    private String error;
    private String message;

    public BusinessException(Integer status, String error, String message) {
        super();
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
