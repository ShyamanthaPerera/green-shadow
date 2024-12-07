package com.example.greenshadow.controlller;

import com.example.greenshadow.customStatusCode.SelectedErrorStatus;
import com.example.greenshadow.dto.MonitoringLogStatus;
import com.example.greenshadow.dto.impl.CropDTO;
import com.example.greenshadow.dto.impl.FieldDTO;
import com.example.greenshadow.dto.impl.MonitoringLogDTO;
import com.example.greenshadow.dto.impl.StaffDTO;
import com.example.greenshadow.entity.impl.MonitoringLogEntity;
import com.example.greenshadow.exception.DataPersistException;
import com.example.greenshadow.exception.LogNotFoundException;
import com.example.greenshadow.service.CropService;
import com.example.greenshadow.service.FieldService;
import com.example.greenshadow.service.LogService;
import com.example.greenshadow.service.StaffService;
import com.example.greenshadow.util.AppUtil;
import com.example.greenshadow.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/log")
@CrossOrigin
public class LogController {

    @Autowired
    private LogService logService;
    @Autowired
    private FieldService fieldService;
    @Autowired
    private CropService cropService;
    @Autowired
    private StaffService staffService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveLog(@RequestParam("logDate") String logDate,
                                        @RequestParam ("logDetails") String logDetails,
                                        @RequestPart("observedImage") MultipartFile observedImage,
                                        @RequestParam (value = "fields",required = false) List<String> fields,
                                        @RequestParam (value = "crops",required = false) List<String> crops,
                                        @RequestParam (value = "staff",required = false) List<String> staffs
    ) {
        String base64ObservedImage = "";
        try {
            List<FieldDTO> field = fieldService.getFieldListByName(fields);
            List<CropDTO> crop = cropService.getCropListByName(crops);
            List<StaffDTO> staff = staffService.getStaffListByName(staffs);
            byte[] byteObservedImage = observedImage.getBytes();
            base64ObservedImage = AppUtil.observedImageOneToBase64(byteObservedImage);

            String log_code = AppUtil.generateLogId();

            MonitoringLogDTO buildMonitoringLogDTO = new MonitoringLogDTO();
            buildMonitoringLogDTO.setLog_code(log_code);
            buildMonitoringLogDTO.setLog_date(logDate);
            buildMonitoringLogDTO.setLog_details(logDetails);
            buildMonitoringLogDTO.setObserved_image(base64ObservedImage);
            buildMonitoringLogDTO.setFields(field);
            buildMonitoringLogDTO.setCrops(crop);
            buildMonitoringLogDTO.setStaff(staff);
            logService.saveLog(buildMonitoringLogDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{logCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public MonitoringLogStatus getSelectedLog(@PathVariable("logCode") String logCode){
        if(!Regex.logCodeMatcher(logCode)){
            return new SelectedErrorStatus(1,"Log code not match");
        }
        return logService.getLog(logCode);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonitoringLogDTO> getAllLogs(){
        return logService.getAllLogs();
    }

    @DeleteMapping(value = "/{logCode}")
    public ResponseEntity<Void> deleteLog(@PathVariable ("logCode") String logCode){
        try {
            if(!Regex.logCodeMatcher(logCode)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            logService.deleteLog(logCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (LogNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/{logCode}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateLog(@PathVariable ("logCode") String logCode,
                                          @RequestParam ("logDate") String logDate,
                                          @RequestParam ("logDetails") String logDetails,
                                          @RequestPart ("observedImage") MultipartFile observedImage,
                                          @RequestParam (value = "fields",required = false) List<String> fields,
                                          @RequestParam (value = "crops",required = false) List<String> crops,
                                          @RequestParam (value = "staff",required = false) List<String> staffs
    ){
        String base64ObservedImage = "";
        try {
            List<FieldDTO> field = fieldService.getFieldListByName(fields);
            List<CropDTO> crop = cropService.getCropListByName(crops);
            List<StaffDTO> staff = staffService.getStaffListByName(staffs);
            byte[] byteObservedImage = observedImage.getBytes();
            base64ObservedImage = AppUtil.observedImageOneToBase64(byteObservedImage);

            String log_code = AppUtil.generateLogId();

            MonitoringLogDTO buildMonitoringLogDTO = new MonitoringLogDTO();

            buildMonitoringLogDTO.setLog_date(logDate);
            buildMonitoringLogDTO.setLog_details(logDetails);
            buildMonitoringLogDTO.setObserved_image(base64ObservedImage);
            buildMonitoringLogDTO.setFields(field);
            buildMonitoringLogDTO.setCrops(crop);
            buildMonitoringLogDTO.setStaff(staff);
            logService.updateLog(logCode, buildMonitoringLogDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getlogcode/{logDesc}")
    public ResponseEntity<String> getLogCode(@PathVariable("logDesc") String logDesc){
        try {
            Optional<MonitoringLogEntity> logEntity = logService.findByLogDesc(logDesc);
            return ResponseEntity.ok(logEntity.get().getLog_code());
        }catch (LogNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
