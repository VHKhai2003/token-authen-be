package com.example.demoweb.exception;

import com.example.demoweb.dto.response.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorMessage handleInvalidArgumentException(MethodArgumentNotValidException exception, WebRequest request) {
        // handle invalid input
        // send error to client
        log.error(exception.getAllErrors().get(0).getDefaultMessage());
        ErrorMessage.Payload payload = ErrorMessage.Payload.builder()
                .status(400)
                .message(exception.getAllErrors().get(0).getDefaultMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(new Date())
                .build();
        return new ErrorMessage(payload, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ErrorMessage handleCustomException(CustomException exception, WebRequest request) {
        // handle custom exception and send error response to client
        log.error(exception.toString());
        ErrorMessage.Payload payload = ErrorMessage.Payload.builder()
                .status(exception.getStatusCode())
                .message(exception.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(new Date())
                .build();
        return new ErrorMessage(payload, HttpStatusCode.valueOf(exception.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    public ErrorMessage handleException(Exception exception, WebRequest request) {
        // handle other exception and send error to client
        exception.printStackTrace();
        ErrorMessage.Payload payload = ErrorMessage.Payload.builder()
                .timestamp(new Date())
                .status(500)
                .message("Internal server error")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        return new ErrorMessage(payload, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
