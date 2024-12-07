package com.example.greenshadow.service;

import com.example.greenshadow.dto.impl.UserDTO;
import com.example.greenshadow.secure.JWTAuthResponse;
import com.example.greenshadow.secure.SignIn;

public interface AuthService {

    JWTAuthResponse signIn(SignIn signIn);

    JWTAuthResponse signUp(UserDTO userDTO);

    JWTAuthResponse refreshToken(String accessToken);
}
