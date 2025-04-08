package com.example.demoweb.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class SuccessfulResponse extends ResponseEntity<SuccessfulResponse.Payload> {
    public SuccessfulResponse(HttpStatusCode statusCode, String message) {
        super(new Payload(statusCode.value(), message), HttpStatus.OK);
    }
    public SuccessfulResponse(HttpStatusCode statusCode, String message, Object data) {
        super(new Payload(statusCode.value(), message, data), HttpStatus.OK);
    }

    @Getter
    @AllArgsConstructor
    public static class Payload {
        private Integer status;
        private String message;
        private Object data;
        public Payload(Integer status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
