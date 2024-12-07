package com.example.greenshadow.service.impl;

import com.example.greenshadow.customStatusCode.SelectedErrorStatus;
import com.example.greenshadow.dao.CropDao;
import com.example.greenshadow.dto.CropStatus;
import com.example.greenshadow.dto.impl.CropDTO;
import com.example.greenshadow.dto.impl.FieldDTO;
import com.example.greenshadow.entity.impl.CropEntity;
import com.example.greenshadow.entity.impl.FieldEntity;
import com.example.greenshadow.exception.CropNotFoundException;
import com.example.greenshadow.exception.DataPersistException;
import com.example.greenshadow.service.CropService;
import com.example.greenshadow.util.AppUtil;
import com.example.greenshadow.util.Mapping;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CropServiceImpl implements CropService {

    @Autowired
    private CropDao cropDao;
    @Autowired
    private Mapping mapping;

    @Override
    public void saveCrop(CropDTO cropDTO) {
        cropDTO.setCrop_code(AppUtil.generateCropId());
        CropEntity saveCrop = cropDao.save(mapping.toCropEntity(cropDTO));
        if(saveCrop == null) {
            throw new DataPersistException("Crop not saved");
        }
    }

    @Override
    public List<CropDTO> getAllCrops() {
        List<CropEntity> crops = cropDao.findAll();
        return crops.stream()
                .map(crop -> {
                    CropDTO cropDTO = new CropDTO();
                    cropDTO.setCrop_image(crop.getCrop_image());
                    cropDTO.setCommon_name(crop.getCommon_name());
                    cropDTO.setScientific_name(crop.getScientific_name());
                    cropDTO.setCategory(crop.getCategory());
                    cropDTO.setSeason(crop.getSeason());
                    FieldDTO fieldDTO = Optional.ofNullable(crop.getField())
                            .map(field -> {
                                FieldDTO minimalFieldDTO = new FieldDTO();
                                minimalFieldDTO.setField_name(field.getField_name());
                                return minimalFieldDTO;
                            })
                            .orElse(null);
                    cropDTO.setField(fieldDTO);
                    return cropDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CropStatus getCrop(String cropCode) {
        if(cropDao.existsById(cropCode)) {
            var selectedCrop = cropDao.getReferenceById(cropCode);
            return mapping.toCropDTO(selectedCrop);
        }else {
            return new SelectedErrorStatus(2,"Selected crop does not exist");
        }
    }

    @Override
    public void deleteCrop(String cropCode) {
        Optional<CropEntity> foundCrop = cropDao.findById(cropCode);
        if(!foundCrop.isPresent()) {
            throw new CropNotFoundException("Crop not found");
        }else {
            cropDao.deleteById(cropCode);
        }
    }

    @Override
    public void updateCrop(String commonName, CropDTO cropDTO) {
        Optional<CropEntity> tmpCrop = cropDao.findByCropName(commonName);
        if(!tmpCrop.isPresent()) {
            throw new CropNotFoundException("Crop not found");
        }else {
            tmpCrop.get().setCommon_name(cropDTO.getCommon_name());
            tmpCrop.get().setScientific_name(cropDTO.getScientific_name());
            tmpCrop.get().setCrop_image(cropDTO.getCrop_image());
            tmpCrop.get().setCategory(cropDTO.getCategory());
            tmpCrop.get().setSeason(cropDTO.getSeason());
            FieldEntity fieldEntity = mapping.toFieldEntity(cropDTO.getField());
            tmpCrop.get().setField(fieldEntity);
        }
    }

    @Override
    public List<String> getAllCropNames() {
        List<CropEntity> cropEntities = cropDao.findAll();
        return cropEntities.stream()
                .map(CropEntity::getCommon_name)
                .collect(Collectors.toList());
    }

    @Override
    public List<CropDTO> getCropListByName(List<String> crops) {
        if(crops.isEmpty() || crops == null) {
            return Collections.emptyList();
        }
        List<CropEntity> cropEntities = cropDao.findByCropNameList(crops);

        if(cropEntities.isEmpty()) {
            throw new CropNotFoundException("Crop not found");
        }
        return cropEntities.stream()
                .map(mapping::toCropDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CropEntity> findByCommonName(String commonName) {
        return cropDao.findByCropName(commonName);
    }
}
