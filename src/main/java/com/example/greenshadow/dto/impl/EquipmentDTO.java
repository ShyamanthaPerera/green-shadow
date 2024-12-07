package com.example.greenshadow.dto.impl;


import com.example.greenshadow.dto.EquipmentStatus;
import com.example.greenshadow.entity.EquipmentType;
import com.example.greenshadow.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EquipmentDTO implements EquipmentStatus {

    private String equipment_id;
    private String name;
    private EquipmentType type;
    private Status status;
    private StaffDTO assigned_staff;
    private FieldDTO assigned_field;
}
