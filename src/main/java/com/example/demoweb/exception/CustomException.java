package com.example.demoweb.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private int statusCode;
    private String message;
    public CustomException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }
}
