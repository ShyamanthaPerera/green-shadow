package com.example.greenshadow.dao;

import com.example.greenshadow.entity.impl.MonitoringLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MonitoringLogDao extends JpaRepository<MonitoringLogEntity,String> {

    @Query("SELECT l FROM MonitoringLogEntity l WHERE l.log_details = :logDesc")
    Optional<MonitoringLogEntity> findByLogDesc(@Param("logDesc") String logDesc);
}
