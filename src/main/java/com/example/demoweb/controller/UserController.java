package com.example.demoweb.controller;

import com.example.demoweb.dto.request.UserLoginDto;
import com.example.demoweb.dto.request.UserRequestDto;
import com.example.demoweb.dto.response.AuthResponseDto;
import com.example.demoweb.dto.response.SuccessfulResponse;
import com.example.demoweb.dto.response.UserResponseDto;
import com.example.demoweb.exception.CustomException;
import com.example.demoweb.service.UserService;
import com.example.demoweb.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // this method will use for wake up the backend hosting server
    @GetMapping("/welcome")
    public String wakeUpSystem() {
        return "Welcome!";
    }


    @PostMapping("/register")
    public SuccessfulResponse addUser(@Valid @RequestBody UserRequestDto userDto) {
        return new SuccessfulResponse(HttpStatus.OK, "Register successfully", userService.addUser(userDto));
    }

    @PostMapping("/login")
    public SuccessfulResponse login(@Valid @RequestBody UserLoginDto userLoginDto) {
        Authentication authentication = null;
        try {
            // using UserDetailsService to load user by email (instead of username)
            // using PasswordEncoder to compare password
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new CustomException(401 ,"Incorrect email or password");
        }

        // final UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDto.getEmail());
        // phuong thuc authenticate cua authenticationManager tra ve mot doi tuong Authentication
        // Ta co the dung do de lay userDetails de toi uu hoa, do mat cong load userDetails lan nua
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AuthResponseDto data = AuthResponseDto.builder()
                .accessToken(jwtUtil.generateToken(userDetails.getUsername()))
                .email(userDetails.getUsername())   // username of userDatails is the email of the user
                .build();
        return new SuccessfulResponse(HttpStatus.OK, "Login successfully", data);
    }

    @GetMapping("/profile")
    public SuccessfulResponse getProfile(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // get user info and return
        UserResponseDto userInfo = userService.getUserByEmail(userDetails.getUsername());
        return new SuccessfulResponse(HttpStatus.OK, "Get user profile successfully", userInfo);
    }

}
