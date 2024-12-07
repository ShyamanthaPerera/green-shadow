package com.example.greenshadow.service.impl;

import com.example.greenshadow.customStatusCode.SelectedErrorStatus;
import com.example.greenshadow.dao.EquipmentDao;
import com.example.greenshadow.dto.EquipmentStatus;
import com.example.greenshadow.dto.impl.EquipmentDTO;
import com.example.greenshadow.dto.impl.FieldDTO;
import com.example.greenshadow.dto.impl.StaffDTO;
import com.example.greenshadow.entity.impl.EquipmentEntity;
import com.example.greenshadow.entity.impl.FieldEntity;
import com.example.greenshadow.entity.impl.StaffEntity;
import com.example.greenshadow.exception.DataPersistException;
import com.example.greenshadow.exception.EquipmentNotFoundException;
import com.example.greenshadow.service.EquipmentService;
import com.example.greenshadow.util.AppUtil;
import com.example.greenshadow.util.Mapping;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentDao equipmentDao;
    @Autowired
    private Mapping mapping;

    @Override
    public void saveEquipment(EquipmentDTO equipmentDTO) {
        equipmentDTO.setEquipment_id(AppUtil.generateEquipmentId());
        EquipmentEntity saveEquipment = equipmentDao.save(mapping.toEquipmentEntity(equipmentDTO));
        if(saveEquipment == null) {
            throw new DataPersistException("Equipment not saved");
        }
    }

    @Override
    public List<EquipmentDTO> getAllEquipment() {
        List<EquipmentEntity> equipments = equipmentDao.findAll();
        return equipments.stream()
                .map(equipment -> {
                    EquipmentDTO equipmentDTO = new EquipmentDTO();
                    equipmentDTO.setName(equipment.getName());
                    equipmentDTO.setType(equipment.getType());
                    equipmentDTO.setStatus(equipment.getStatus());
                    StaffDTO staffDTO = Optional.ofNullable(equipment.getAssigned_staff())
                            .map(staff -> {
                                StaffDTO minimalStaffDto = new StaffDTO();
                                minimalStaffDto.setFirst_name(staff.getFirst_name());
                                return minimalStaffDto;
                            })
                            .orElse(null);
                    FieldDTO fieldDTO = Optional.ofNullable(equipment.getAssigned_field())
                            .map(field -> {
                                FieldDTO minimalFieldDTO = new FieldDTO();
                                minimalFieldDTO.setField_name(field.getField_name());
                                return minimalFieldDTO;
                            })
                            .orElse(null);
                    equipmentDTO.setAssigned_staff(staffDTO);
                    equipmentDTO.setAssigned_field(fieldDTO);
                    return equipmentDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public EquipmentStatus getEquipment(String equipmentId) {
        if(equipmentDao.existsById(equipmentId)) {
            var selectedEquipment = equipmentDao.getReferenceById(equipmentId);
            return mapping.toEquipmentDTO(selectedEquipment);
        }else{
            return new SelectedErrorStatus(2,"Selected Equipment not found");
        }
    }

    @Override
    public void deleteEquipment(String equipmentId) {
        Optional<EquipmentEntity> foundEquipment = equipmentDao.findById(equipmentId);
        if(!foundEquipment.isPresent()) {
            throw new EquipmentNotFoundException("Equipment not found");
        }else{
            equipmentDao.deleteById(equipmentId);
        }
    }

    @Override
    public void updateEquipment(String equipmentId, EquipmentDTO equipmentDTO) {
        Optional<EquipmentEntity> tmpEquipment = equipmentDao.findById(equipmentId);
        if(!tmpEquipment.isPresent()) {
            throw new EquipmentNotFoundException("Equipment not found");
        }else{
            tmpEquipment.get().setName(equipmentDTO.getName());
            tmpEquipment.get().setType(equipmentDTO.getType());
            tmpEquipment.get().setStatus(equipmentDTO.getStatus());
            StaffEntity staffEntity = mapping.toStaffEntity(equipmentDTO.getAssigned_staff());
            tmpEquipment.get().setAssigned_staff(staffEntity);
            FieldEntity fieldEntity = mapping.toFieldEntity(equipmentDTO.getAssigned_field());
            tmpEquipment.get().setAssigned_field(fieldEntity);
        }
    }

    @Override
    public Optional<EquipmentEntity> findByEquipName(String equipmentName) {
        return equipmentDao.findByEquipmentName(equipmentName);
    }
}
