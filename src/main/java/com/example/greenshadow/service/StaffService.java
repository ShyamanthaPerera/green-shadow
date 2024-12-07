package com.example.greenshadow.service;

import com.example.greenshadow.dto.impl.StaffDTO;
import com.example.greenshadow.entity.impl.StaffEntity;

import java.util.List;
import java.util.Optional;

public interface StaffService {

    void saveStaff(StaffDTO staffDTO);

    List<StaffDTO> getAllStaff();

    void deleteStaff(String id);

    void updateStaff(String staffId,StaffDTO staffDTO);

    List<String> getAllStaffNames();

    List<StaffDTO> getStaffListByName(List<String> staffs);

    StaffDTO getStaffByName(String assignedStaff);

    Optional<StaffEntity> findByFirstName(String firstName);
}
