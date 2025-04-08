package com.example.demoweb.service.impl;

import com.example.demoweb.dto.request.UserRequestDto;
import com.example.demoweb.dto.response.UserResponseDto;
import com.example.demoweb.entity.User;
import com.example.demoweb.exception.CustomException;
import com.example.demoweb.repository.UserRepository;
import com.example.demoweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserResponseDto addUser(UserRequestDto userDto) {

        // check password and confirmed password is the same
        if(!userDto.getPassword().equals(userDto.getConfirmedPassword())) {
            throw new CustomException(400, "Confirmed password is not identical to password");
        }

        // check username and email are unique
        if(userRepository.existsByUsername(userDto.getUsername())) {
            throw new CustomException(400, "Username is exist");
        }
        if(userRepository.existsByEmail(userDto.getEmail())) {
            throw new CustomException(400, "Email is exist");
        }

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .build();
        // add user to database and return UserResponseDto to controller
        User addedUser = userRepository.save(user);
        return UserResponseDto.builder()
                .id(addedUser.getId())
                .username(addedUser.getUsername())
                .email(addedUser.getEmail())
                .createdAt(addedUser.getCreatedAt())
                .build();
    }
}
