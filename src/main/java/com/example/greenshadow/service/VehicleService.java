package com.example.greenshadow.service;

import com.example.greenshadow.dto.VehicleStatus;
import com.example.greenshadow.dto.impl.VehicleDTO;
import com.example.greenshadow.entity.impl.VehicleEntity;

import java.util.List;
import java.util.Optional;

public interface VehicleService {

    void saveVehicle(VehicleDTO vehicleDTO);

    List<VehicleDTO> getAllVehicles();

    VehicleStatus getVehicle(String vehicleCode);

    void deleteVehicle(String vehicleCode);

    void updateVehicle(String vehicleCode,VehicleDTO vehicleDTO);

    Optional<VehicleEntity> findByLicenseNumber(String licenseNumber);
}
