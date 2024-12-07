package com.example.greenshadow.service.impl;

import com.example.greenshadow.customStatusCode.SelectedErrorStatus;
import com.example.greenshadow.dao.MonitoringLogDao;
import com.example.greenshadow.dto.MonitoringLogStatus;
import com.example.greenshadow.dto.impl.MonitoringLogDTO;
import com.example.greenshadow.entity.impl.CropEntity;
import com.example.greenshadow.entity.impl.FieldEntity;
import com.example.greenshadow.entity.impl.MonitoringLogEntity;
import com.example.greenshadow.entity.impl.StaffEntity;
import com.example.greenshadow.exception.DataPersistException;
import com.example.greenshadow.exception.LogNotFoundException;
import com.example.greenshadow.service.LogService;
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
public class LogServiceImpl implements LogService {

    @Autowired
    private MonitoringLogDao monitoringLogDao;
    @Autowired
    private Mapping mapping;

    @Override
    public void saveLog(MonitoringLogDTO monitoringLogDTO) {
        monitoringLogDTO.setLog_code(AppUtil.generateLogId());
        MonitoringLogEntity saveLog = monitoringLogDao.save(mapping.toMonitoringLogEntity
                (monitoringLogDTO));
        if(saveLog == null){
            throw new DataPersistException("Log not saved");
        }
    }

    @Override
    public List<MonitoringLogDTO> getAllLogs() {
        List<MonitoringLogEntity> logs = monitoringLogDao.findAll();
        return logs.stream()
                .map(log -> {
                    MonitoringLogDTO monitoringLogDTO = new MonitoringLogDTO();
                    monitoringLogDTO.setLog_date(log.getLog_date());
                    monitoringLogDTO.setLog_details(log.getLog_details());
                    monitoringLogDTO.setObserved_image(log.getObserved_image());
                    return monitoringLogDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public MonitoringLogStatus getLog(String logCode) {
        if(monitoringLogDao.existsById(logCode)){
            var selectedLog = monitoringLogDao.getReferenceById(logCode);
            return (MonitoringLogStatus) mapping.toMonitoringLogDTO(selectedLog);
        }else{
            return new SelectedErrorStatus(2,"Selected Log not found");
        }
    }

    @Override
    public void deleteLog(String logCode) {
        Optional<MonitoringLogEntity> foundLog = monitoringLogDao.findById(logCode);
        if(!foundLog.isPresent()){
            throw new LogNotFoundException("Log not found");
        }else{
            monitoringLogDao.deleteById(logCode);
        }
    }

    @Override
    public void updateLog(String logCode, MonitoringLogDTO monitoringLogDTO) {
        Optional<MonitoringLogEntity> tmpLog = monitoringLogDao.findById(logCode);
        if(!tmpLog.isPresent()){
            throw new LogNotFoundException("Log not found");
        }else{
            tmpLog.get().setLog_date(monitoringLogDTO.getLog_date());
            tmpLog.get().setLog_details(monitoringLogDTO.getLog_details());
            tmpLog.get().setObserved_image(monitoringLogDTO.getObserved_image());
            List<FieldEntity> fieldEntityList = mapping.toFieldEntityList(monitoringLogDTO.getFields());
            tmpLog.get().setFields(fieldEntityList);
            List<CropEntity> cropEntityList = mapping.toCropEntityList(monitoringLogDTO.getCrops());
            tmpLog.get().setCrops(cropEntityList);
            List<StaffEntity> staffEntityList = mapping.toStaffEntityList(monitoringLogDTO.getStaff());
            tmpLog.get().setStaff(staffEntityList);
        }
    }

    @Override
    public Optional<MonitoringLogEntity> findByLogDesc(String logDesc) {
        return monitoringLogDao.findByLogDesc(logDesc);
    }
}
