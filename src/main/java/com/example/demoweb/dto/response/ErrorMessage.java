package com.example.demoweb.dto.response;

import lombok.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public class ErrorMessage extends ResponseEntity<ErrorMessage.Payload> {
    public ErrorMessage(Payload body, HttpStatusCode status) {
        super(body, status);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Payload {
        private Integer status;
        private String message;
        private String path;
        private Date timestamp;
    }
}
