package com.example.greenshadow.dao;

import com.example.greenshadow.entity.impl.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StaffDao extends JpaRepository<StaffEntity,String> {

    @Query("SELECT s FROM StaffEntity s WHERE s.first_name IN :staffs")
    List<StaffEntity> findByStaffNameList(@Param("staffs") List<String> staffs);

    @Query("SELECT s FROM StaffEntity s WHERE s.first_name = :first_name")
    Optional<StaffEntity> findByStaffName(@Param("first_name") String assignedStaff);
}
