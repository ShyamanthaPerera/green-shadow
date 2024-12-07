package com.example.greenshadow.dto.impl;


import com.example.greenshadow.dto.CropStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CropDTO implements CropStatus {

    private String crop_code;
    private String common_name;
    private String scientific_name;
    private String crop_image;
    private String category;
    private String season;
    private FieldDTO field;
}
