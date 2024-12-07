package com.example.greenshadow.service;

import com.example.greenshadow.dto.EquipmentStatus;
import com.example.greenshadow.dto.impl.EquipmentDTO;
import com.example.greenshadow.entity.impl.EquipmentEntity;

import java.util.List;
import java.util.Optional;

public interface EquipmentService {

    void saveEquipment(EquipmentDTO equipmentDTO);

    List<EquipmentDTO> getAllEquipment();

    EquipmentStatus getEquipment(String equipmentId);

    void deleteEquipment(String equipmentId);

    void updateEquipment(String equipmentId,EquipmentDTO equipmentDTO);

    Optional<EquipmentEntity> findByEquipName(String equipmentName);
}
