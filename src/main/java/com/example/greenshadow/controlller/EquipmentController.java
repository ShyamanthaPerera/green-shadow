package com.example.greenshadow.controlller;

import com.example.greenshadow.customStatusCode.SelectedErrorStatus;
import com.example.greenshadow.dto.EquipmentStatus;
import com.example.greenshadow.dto.impl.EquipmentDTO;
import com.example.greenshadow.dto.impl.FieldDTO;
import com.example.greenshadow.dto.impl.StaffDTO;
import com.example.greenshadow.entity.impl.EquipmentEntity;
import com.example.greenshadow.exception.DataPersistException;
import com.example.greenshadow.exception.EquipmentNotFoundException;
import com.example.greenshadow.service.EquipmentService;
import com.example.greenshadow.service.FieldService;
import com.example.greenshadow.service.StaffService;
import com.example.greenshadow.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/equipment")
@CrossOrigin
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private FieldService fieldService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        try {
            StaffDTO staff = staffService.getStaffByName(equipmentDTO.getAssigned_staff().getFirst_name());
            FieldDTO field = fieldService.getFieldByName(equipmentDTO.getAssigned_field().getField_name());
            equipmentDTO.setAssigned_staff(staff);
            equipmentDTO.setAssigned_field(field);
            equipmentService.saveEquipment(equipmentDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/{equipmentId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public EquipmentStatus getSelectedEquipment(@PathVariable("equipmentId") String equipmentId) {
        if (!Regex.equipIdMatcher(equipmentId)) {
            return new SelectedErrorStatus(1, "Equipment id does not match");
        }
        return equipmentService.getEquipment(equipmentId);
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EquipmentDTO> getAllEquipments() {
        return equipmentService.getAllEquipment();
    }

    @DeleteMapping(value = "/{equipmentId}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable("equipmentId") String equipmentId) {
        try {
            if(!Regex.equipIdMatcher(equipmentId)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            equipmentService.deleteEquipment(equipmentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (EquipmentNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PatchMapping(value = "/{equipmentId}")
    public ResponseEntity<Void> updateEquipment(@PathVariable ("equipmentId") String equipmentId,
                                                @RequestBody EquipmentDTO equipmentDTO) {

        try {
            if(!Regex.equipIdMatcher(equipmentId) || equipmentDTO == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            StaffDTO staff = staffService.getStaffByName(equipmentDTO.getAssigned_staff().getFirst_name());
            FieldDTO field = fieldService.getFieldByName(equipmentDTO.getAssigned_field().getField_name());
            equipmentDTO.setAssigned_staff(staff);
            equipmentDTO.setAssigned_field(field);
            equipmentService.updateEquipment(equipmentId, equipmentDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (EquipmentNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getequipId/{equipmentName}")
    public ResponseEntity<String> getEquipId(@PathVariable("equipmentName") String equipmentName) {
        try {
            Optional<EquipmentEntity> equipmentEntity = equipmentService.findByEquipName(equipmentName);
            return ResponseEntity.ok(equipmentEntity.get().getEquipment_id());
        }catch (EquipmentNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
