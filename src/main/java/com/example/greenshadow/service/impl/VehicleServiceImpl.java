package com.example.greenshadow.service.impl;

import com.example.greenshadow.customStatusCode.SelectedErrorStatus;
import com.example.greenshadow.dao.VehicleDao;
import com.example.greenshadow.dto.VehicleStatus;
import com.example.greenshadow.dto.impl.StaffDTO;
import com.example.greenshadow.dto.impl.VehicleDTO;
import com.example.greenshadow.entity.impl.StaffEntity;
import com.example.greenshadow.entity.impl.VehicleEntity;
import com.example.greenshadow.exception.DataPersistException;
import com.example.greenshadow.exception.VehicleNotFoundException;
import com.example.greenshadow.service.VehicleService;
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
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private Mapping mapping;

    @Override
    public void saveVehicle(VehicleDTO vehicleDTO) {
        vehicleDTO.setVehicle_code(AppUtil.generateVehicleId());
        VehicleEntity saveVehicle = vehicleDao.save(mapping.toVehicleEntity(vehicleDTO));
        if(saveVehicle == null) {
            throw new DataPersistException("Vehicle not saved");
        }
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        List<VehicleEntity> vehicles = vehicleDao.findAll();
        return vehicles.stream()
                .map(vehicle -> {
                    VehicleDTO vehicleDTO = new VehicleDTO();
                    vehicleDTO.setLicensePlateNumber(vehicle.getLicensePlateNumber());
                    vehicleDTO.setVehicleCategory(vehicle.getVehicleCategory());
                    vehicleDTO.setFuelType(vehicle.getFuelType());
                    vehicleDTO.setStatus(vehicle.getStatus());
                    vehicleDTO.setRemarks(vehicle.getRemarks());
                    StaffDTO staffDTO = Optional.ofNullable(vehicle.getAssigned_staff())
                            .map(staff -> {
                                StaffDTO minimalStaffDto = new StaffDTO();
                                minimalStaffDto.setFirst_name(staff.getFirst_name());
                                return minimalStaffDto;
                            })
                            .orElse(null);
                    vehicleDTO.setAssigned_staff(staffDTO);
                    return vehicleDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public VehicleStatus getVehicle(String vehicleCode) {
        if(vehicleDao.existsById(vehicleCode)) {
            var selectedVehicle = vehicleDao.getReferenceById(vehicleCode);
            return (VehicleStatus) mapping.toVehicleDTO(selectedVehicle);
        }else {
            return new SelectedErrorStatus(2,"Selected Vehicle Not Found");
        }
    }

    @Override
    public void deleteVehicle(String vehicleCode) {
        Optional<VehicleEntity> foundVehicle = vehicleDao.findById(vehicleCode);
        if(!foundVehicle.isPresent()) {
            throw new VehicleNotFoundException("Vehicle Not Found");
        }else{
            vehicleDao.deleteById(vehicleCode);
        }
    }

    @Override
    public void updateVehicle(String vehicleCode, VehicleDTO vehicleDTO) {
        Optional<VehicleEntity> tmpVehicle = vehicleDao.findById(vehicleCode);
        if(!tmpVehicle.isPresent()) {
            throw new VehicleNotFoundException("Vehicle Not Found");
        }else {
            tmpVehicle.get().setLicensePlateNumber(vehicleDTO.getLicensePlateNumber());
            tmpVehicle.get().setVehicleCategory(vehicleDTO.getVehicleCategory());
            tmpVehicle.get().setFuelType(vehicleDTO.getFuelType());
            tmpVehicle.get().setStatus(vehicleDTO.getStatus());
            tmpVehicle.get().setRemarks(vehicleDTO.getRemarks());
            StaffEntity staffEntity = mapping.toStaffEntity(vehicleDTO.getAssigned_staff());
            tmpVehicle.get().setAssigned_staff(staffEntity);
        }
    }

    @Override
    public Optional<VehicleEntity> findByLicenseNumber(String licenseNumber) {
        return vehicleDao.findByLicenseNumber(licenseNumber);
    }
}
