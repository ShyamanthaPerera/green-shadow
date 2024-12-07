package com.example.greenshadow.dto.impl;

import com.example.greenshadow.dto.FieldStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldDTO implements FieldStatus {

    private String field_code;
    private String field_name;
    private Point location;
    private Double extent_size;
    private String field_image1;
    private String field_image2;
    private java.util.List<CropDTO> crops = new ArrayList<>();
    private List<StaffDTO> allocated_staff = new ArrayList<>();
}
