package com.example.demoweb.service;

import com.example.demoweb.dto.request.UserRequestDto;
import com.example.demoweb.dto.response.UserResponseDto;
import com.example.demoweb.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    UserResponseDto addUser(UserRequestDto userDto);
}
