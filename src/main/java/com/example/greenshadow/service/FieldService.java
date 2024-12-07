package com.example.greenshadow.service;

import com.example.greenshadow.dto.FieldStatus;
import com.example.greenshadow.dto.impl.FieldDTO;
import com.example.greenshadow.entity.impl.FieldEntity;

import java.util.List;
import java.util.Optional;

public interface FieldService {

    void saveField(FieldDTO fieldDTO);

    List<FieldDTO> getAllFields();

    FieldStatus getField(String fieldCode);

    void deleteField(String fieldCode);

    void updateField(String fieldName,FieldDTO fieldDTO);

    void updateAllocatedStaff(String fieldCode, List<String> staffId);

    List<String> getAllFieldNames();

    FieldDTO getFieldByName(String field_name);

    List<FieldDTO> getFieldListByName(List<String> field_name);

    Optional<FieldEntity> findByFieldName(String fieldName);
}
