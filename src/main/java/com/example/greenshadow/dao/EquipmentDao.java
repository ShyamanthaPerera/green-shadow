package com.example.greenshadow.dao;

import com.example.greenshadow.entity.impl.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EquipmentDao extends JpaRepository<EquipmentEntity,String> {

    @Query("SELECT e FROM EquipmentEntity e WHERE e.name = :equipmentName")
    Optional<EquipmentEntity> findByEquipmentName(@Param("equipmentName") String equipmentName);
}
