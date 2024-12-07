package com.example.greenshadow.controlller;

import com.example.greenshadow.dto.impl.StaffDTO;
import com.example.greenshadow.dto.impl.FieldDTO;
import com.example.greenshadow.entity.impl.StaffEntity;
import com.example.greenshadow.exception.DataPersistException;
import com.example.greenshadow.exception.StaffNotFoundException;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/staff")
@CrossOrigin
public class StaffController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private FieldService fieldService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveStaff(@RequestBody StaffDTO staffDTO) {
        try {
            List<String> field_name = staffDTO.getFields()
                    .stream()
                    .map(FieldDTO::getField_name)
                    .collect(Collectors.toList());
            List<FieldDTO> fields = fieldService.getFieldListByName(field_name);
            staffDTO.setFields(fields);
            staffService.saveStaff(staffDTO);
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
    public List<StaffDTO> getAllStaff(){
        return staffService.getAllStaff();
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable("id") String id){
        try {
            if(!Regex.staffIdMatcher(id)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            staffService.deleteStaff(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (StaffNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{staffId}")
    public ResponseEntity<Void> updateStaff(@PathVariable ("staffId") String staffId,
                                            @RequestBody StaffDTO staffDTO){

        try {
            if(!Regex.staffIdMatcher(staffId) || staffDTO == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            List<String> field_name = staffDTO.getFields()
                    .stream()
                    .map(FieldDTO::getField_name)
                    .collect(Collectors.toList());
            List<FieldDTO> fields = fieldService.getFieldListByName(field_name);
            staffDTO.setFields(fields);
            staffService.updateStaff(staffId, staffDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (StaffNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "getallstaffnames",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAllStaffName(){
        List<String> staffNames = staffService.getAllStaffNames();
        return ResponseEntity.ok(staffNames);
    }


    @GetMapping( "/getstaffid/{firstName}")
    public ResponseEntity<String> getStaffId(@PathVariable("firstName") String firstName){
        try {
            Optional<StaffEntity> staffEntity = staffService.findByFirstName(firstName);
            return ResponseEntity.ok(staffEntity.get().getId());
        }catch (StaffNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
