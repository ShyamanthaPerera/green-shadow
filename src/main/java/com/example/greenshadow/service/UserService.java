package com.example.greenshadow.service;

import com.example.greenshadow.dto.impl.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    void saveUser(UserDTO userDTO);

    UserDetailsService userDetailsService();
}
