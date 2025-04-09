package com.example.demoweb.service.impl;

import com.example.demoweb.entity.User;
import com.example.demoweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // loadUserByUserName is a method defined in UserDetailsService
    // Remember: this system use email to identify a user
    // ==> use email instead of username
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found!"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // use email instead of username
                .password(user.getPassword())
                .build();
    }
}
