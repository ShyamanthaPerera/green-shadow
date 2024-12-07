package com.example.greenshadow.controlller;

import com.example.greenshadow.customStatusCode.SelectedErrorStatus;
import com.example.greenshadow.dto.FieldStatus;
import com.example.greenshadow.dto.impl.CropDTO;
import com.example.greenshadow.dto.impl.FieldDTO;
import com.example.greenshadow.dto.impl.StaffDTO;
import com.example.greenshadow.entity.impl.FieldEntity;
import com.example.greenshadow.exception.DataPersistException;
import com.example.greenshadow.exception.FieldNotFoundException;
import com.example.greenshadow.service.FieldService;
import com.example.greenshadow.util.AppUtil;
import com.example.greenshadow.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/v1/field")
@CrossOrigin
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveField(@RequestParam("field_name") String fieldName,
                                          @RequestParam ("x") int x,
                                          @RequestParam ("y") int y,
                                          @RequestParam ("extent_size") String size,
                                          @RequestPart("field_image1") MultipartFile fieldImage1,
                                          @RequestPart ("field_image2") MultipartFile fieldImage2,
                                          @RequestPart (value = "crops[]",required = false) List<CropDTO> crops,
                                          @RequestPart (value = "staff[]",required = false) List<StaffDTO> staff
    ) {
        String base64FieldImage1 = "";
        String base64FieldImage2 = "";
        Point location = new Point(x,y);
        double extentSize = Double.parseDouble(size);

        try {
            byte[] bytesFieldImage1 = fieldImage1.getBytes();
            base64FieldImage1 = AppUtil.fieldImageOneToBase64(bytesFieldImage1);

            byte[] bytesFieldImage2 = fieldImage2.getBytes();
            base64FieldImage2 = AppUtil.fieldImageTwoToBase64(bytesFieldImage2);

            String field_code = AppUtil.generateFieldId();

            FieldDTO buildFieldDTO = new FieldDTO();
            buildFieldDTO.setField_code(field_code);
            buildFieldDTO.setField_name(fieldName);
            buildFieldDTO.setLocation(location);
            buildFieldDTO.setExtent_size(extentSize);
            buildFieldDTO.setField_image1(base64FieldImage1);
            buildFieldDTO.setField_image2(base64FieldImage2);
            buildFieldDTO.setCrops(crops);
            buildFieldDTO.setAllocated_staff(staff);
            fieldService.saveField(buildFieldDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{fieldCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public FieldStatus getSelectedField(@PathVariable("fieldCode") String fieldCode) {
        if(!Regex.fieldCodeMatcher(fieldCode)){
            return new SelectedErrorStatus(1,"Field Code Not Matched");
        }
        return fieldService.getField(fieldCode);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FieldDTO> getAllFields() {
        return fieldService.getAllFields();
    }

    @DeleteMapping(value = "/{fieldCode}")
    public ResponseEntity<Void> deleteField(@PathVariable("fieldCode") String fieldCode) {
        try {
            if(!Regex.fieldCodeMatcher(fieldCode)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            fieldService.deleteField(fieldCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (FieldNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{fieldName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateField(@PathVariable @RequestParam ("field_name") String fieldName,
                                            @RequestParam ("x") int x,
                                            @RequestParam ("y") int y,
                                            @RequestParam ("extent_size") String size,
                                            @RequestPart ("field_image1") MultipartFile fieldImage1,
                                            @RequestPart ("field_image2") MultipartFile fieldImage2,
                                            @RequestPart (value = "crops[]",required = false) List<CropDTO> crops,
                                            @RequestPart (value = "staff[]",required = false) List<StaffDTO> staff
    ) {
        String base64FieldImage1 = "";
        String base64FieldImage2 = "";
        Point location = new Point(x,y);
        double extentSize = Double.parseDouble(size);

        try {
            byte[] bytesFieldImage1 = fieldImage1.getBytes();
            base64FieldImage1 = AppUtil.fieldImageOneToBase64(bytesFieldImage1);

            byte[] bytesFieldImage2 = fieldImage2.getBytes();
            base64FieldImage2 = AppUtil.fieldImageTwoToBase64(bytesFieldImage2);

            String field_code = AppUtil.generateFieldId();

            FieldDTO buildFieldDTO = new FieldDTO();
            buildFieldDTO.setField_code(field_code);
            buildFieldDTO.setField_name(fieldName);
            buildFieldDTO.setLocation(location);
            buildFieldDTO.setExtent_size(extentSize);
            buildFieldDTO.setField_image1(base64FieldImage1);
            buildFieldDTO.setField_image2(base64FieldImage2);
            buildFieldDTO.setCrops(crops);
            buildFieldDTO.setAllocated_staff(staff);
            fieldService.updateField(fieldName,buildFieldDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = {"updatestaff","/{fieldCode}"})
    public ResponseEntity<Void> updateAllocatedStaff(@PathVariable ("fieldCode") String fieldCode,
                                                     @RequestBody List<String> staffId) {

        try {
            if(!Regex.fieldCodeMatcher(fieldCode) || staffId == null || staffId.isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            fieldService.updateAllocatedStaff(fieldCode, staffId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (FieldNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "getallfieldnames", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAllFieldName(){
        List<String> fieldNames = fieldService.getAllFieldNames();
        return ResponseEntity.ok(fieldNames);
    }

    @GetMapping("/getfieldcode/{fieldName}")
    public ResponseEntity<String> getFieldCode(@PathVariable("fieldName") String fieldName) {
        try {
            Optional<FieldEntity> fieldEntity = fieldService.findByFieldName(fieldName);
            return ResponseEntity.ok(fieldEntity.get().getField_code());
        }catch (FieldNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
