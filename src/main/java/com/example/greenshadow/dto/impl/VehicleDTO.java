package com.example.greenshadow.dto.impl;

import com.example.greenshadow.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleDTO {

    private String vehicle_code;
    private String licensePlateNumber;
    private String vehicleCategory;
    private String fuelType;
    private Status status;
    private String remarks;
    private StaffDTO assigned_staff;
}
