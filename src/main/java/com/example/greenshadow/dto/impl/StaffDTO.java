package com.example.greenshadow.dto.impl;

import com.example.greenshadow.dto.StaffStatus;
import com.example.greenshadow.entity.Designation;
import com.example.greenshadow.entity.Gender;
import com.example.greenshadow.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StaffDTO implements StaffStatus {

    private String id;
    private String first_name;
    private String last_name;
    private Designation designation;
    private Gender gender;
    private String joined_date;
    private String dob;
    private String address;
    private String contact_no;
    private String email;
    private Role role;
    private List<FieldDTO> fields;
}
