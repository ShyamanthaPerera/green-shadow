package com.example.greenshadow.service.impl;

import com.example.greenshadow.dao.UserDao;
import com.example.greenshadow.dto.impl.UserDTO;
import com.example.greenshadow.entity.impl.UserEntity;
import com.example.greenshadow.secure.JWTAuthResponse;
import com.example.greenshadow.secure.SignIn;
import com.example.greenshadow.service.AuthService;
import com.example.greenshadow.service.JWTService;
import com.example.greenshadow.util.AppUtil;
import com.example.greenshadow.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final Mapping mapping;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public JWTAuthResponse signIn(SignIn signIn) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signIn.getEmail(), signIn.getPassword()));
        var user = userDao.findByEmail(signIn.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Generate token when logged in return it
        var generatedToken = jwtService.generateToken(user);
        System.out.println(generatedToken);
        return JWTAuthResponse.builder().token(generatedToken).build();
    }

    @Override
    public JWTAuthResponse signUp(UserDTO userDTO) {
        userDTO.setUserId(AppUtil.generateUserId());
        UserEntity savedUser = userDao.save(mapping.toUserEntity(userDTO));
        //Generate the token and return it
        var generatedToken = jwtService.generateToken(savedUser);
        System.out.println(generatedToken);
        return JWTAuthResponse.builder().token(generatedToken).build();
    }

    @Override
    public JWTAuthResponse refreshToken(String accessToken) {
        //extract username
        var userName = jwtService.extractUserName(accessToken);
        //check the user availability in the DB
        var findUser = userDao.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var refreshToken = jwtService.refreshToken(findUser);
        return JWTAuthResponse.builder().token(refreshToken).build();
    }
}
