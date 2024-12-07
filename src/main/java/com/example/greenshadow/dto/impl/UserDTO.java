package com.example.greenshadow.dto.impl;

import com.example.greenshadow.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private String userId;
    private String email;
    private String password;
    private Role role;
}
