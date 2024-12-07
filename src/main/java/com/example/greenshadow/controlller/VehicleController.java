package com.example.greenshadow.controlller;

import com.example.greenshadow.dto.impl.StaffDTO;
import com.example.greenshadow.dto.impl.VehicleDTO;
import com.example.greenshadow.entity.impl.VehicleEntity;
import com.example.greenshadow.exception.DataPersistException;
import com.example.greenshadow.exception.VehicleNotFoundException;
import com.example.greenshadow.service.StaffService;
import com.example.greenshadow.service.VehicleService;
import com.example.greenshadow.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/vehicle")
@CrossOrigin
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private StaffService staffService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveVehicle(@RequestBody VehicleDTO vehicleDTO) {
        try {
            StaffDTO staff = staffService.getStaffByName(vehicleDTO.getAssigned_staff().getFirst_name());
            vehicleDTO.setAssigned_staff(staff);
            vehicleService.saveVehicle(vehicleDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleDTO> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @DeleteMapping(value = "/{vehicleCode}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable("vehicleCode") String vehicleCode) {
        try {
            if(!Regex.vehicleCodeMatcher(vehicleCode)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            vehicleService.deleteVehicle(vehicleCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (VehicleNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PatchMapping(value = "/{vehicleCode}")
    public ResponseEntity<Void> updateVehicle(@PathVariable ("vehicleCode") String vehicleCode,
                                              @RequestBody VehicleDTO vehicleDTO) {

        try {
            if(!Regex.vehicleCodeMatcher(vehicleCode) || vehicleDTO == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            StaffDTO staff = staffService.getStaffByName(vehicleDTO.getAssigned_staff().getFirst_name());
            vehicleDTO.setAssigned_staff(staff);
            vehicleService.updateVehicle(vehicleCode, vehicleDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (VehicleNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getvehiclecode/{licenseNumber}")
    public ResponseEntity<String> getVehicleCode(@PathVariable("licenseNumber") String licenseNumber) {
        try {
            Optional<VehicleEntity> vehicleEntity = vehicleService.findByLicenseNumber(licenseNumber);
            return ResponseEntity.ok(vehicleEntity.get().getVehicle_code());
        }catch (VehicleNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
