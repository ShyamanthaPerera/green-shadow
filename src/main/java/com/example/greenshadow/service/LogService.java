package com.example.greenshadow.service;

import com.example.greenshadow.dto.MonitoringLogStatus;
import com.example.greenshadow.dto.impl.MonitoringLogDTO;
import com.example.greenshadow.entity.impl.MonitoringLogEntity;

import java.util.List;
import java.util.Optional;

public interface LogService {

    void saveLog(MonitoringLogDTO monitoringLogDTO);

    List<MonitoringLogDTO> getAllLogs();

    MonitoringLogStatus getLog(String logCode);

    void deleteLog(String logCode);

    void updateLog(String logCode,MonitoringLogDTO monitoringLogDTO);

    Optional<MonitoringLogEntity> findByLogDesc(String logDesc);
}
