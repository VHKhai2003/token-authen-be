package com.example.demoweb.controller;

import com.example.demoweb.dto.request.UserRequestDto;
import com.example.demoweb.dto.response.SuccessfulResponse;
import com.example.demoweb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("")
    public SuccessfulResponse get() {
        return new SuccessfulResponse(HttpStatus.OK, "Get all users successfully", userService.getAllUsers());
    }

    @PostMapping("/register")
    public SuccessfulResponse addUser(@Valid @RequestBody UserRequestDto userDto) {
        return new SuccessfulResponse(HttpStatus.OK, "Register successfully", userService.addUser(userDto));
    }
}
