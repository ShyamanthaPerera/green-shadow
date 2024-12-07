package com.example.greenshadow.service.impl;

import com.example.greenshadow.dao.UserDao;
import com.example.greenshadow.dto.impl.UserDTO;
import com.example.greenshadow.entity.impl.UserEntity;
import com.example.greenshadow.exception.DataPersistException;
import com.example.greenshadow.exception.UserNotFoundException;
import com.example.greenshadow.service.UserService;
import com.example.greenshadow.util.Mapping;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private Mapping mapping;

    @Override
    public void saveUser(UserDTO userDTO) {
        UserEntity saveUser = userDao.save(mapping.toUserEntity(userDTO));
        if(saveUser == null) {
            throw new DataPersistException("User not saved");
        }
    }

    @Override
    public UserDetailsService userDetailsService() {
        return userName ->
                userDao.findByEmail(userName)
                        .orElseThrow(()-> new UserNotFoundException("User Not Found"));
    }
}
