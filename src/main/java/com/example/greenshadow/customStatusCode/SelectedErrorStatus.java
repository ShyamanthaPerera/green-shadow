package com.example.greenshadow.customStatusCode;

import com.example.greenshadow.dto.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SelectedErrorStatus implements CropStatus, FieldStatus, EquipmentStatus, MonitoringLogStatus, VehicleStatus,StaffStatus{

    private int statusCode;
    private String statusMessage;
}
