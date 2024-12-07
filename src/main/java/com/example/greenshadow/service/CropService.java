package com.example.greenshadow.service;

import com.example.greenshadow.dto.CropStatus;
import com.example.greenshadow.dto.impl.CropDTO;
import com.example.greenshadow.entity.impl.CropEntity;

import java.util.List;
import java.util.Optional;

public interface CropService {

    void saveCrop(CropDTO cropDTO);

    List<CropDTO> getAllCrops();

    CropStatus getCrop(String cropCode);

    void deleteCrop(String cropCode);

    void updateCrop(String commonName,CropDTO cropDTO);

    List<String> getAllCropNames();

    List<CropDTO> getCropListByName(List<String> crops);

    Optional<CropEntity> findByCommonName(String commonName);
}
