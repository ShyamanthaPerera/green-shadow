package com.example.greenshadow.dao;

import com.example.greenshadow.entity.impl.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VehicleDao extends JpaRepository<VehicleEntity,String> {

    @Query("SELECT v FROM VehicleEntity v WHERE v.licensePlateNumber = :licenseNumber")
    Optional<VehicleEntity> findByLicenseNumber(@Param("licenseNumber") String licenseNumber);
}
