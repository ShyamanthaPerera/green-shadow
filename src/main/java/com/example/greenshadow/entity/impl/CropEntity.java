package com.example.greenshadow.entity.impl;

import com.example.greenshadow.entity.SuperEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "crop")
public class CropEntity implements SuperEntity {

    @Id
    private String crop_code;
    private String common_name;
    private String scientific_name;
    @Column(columnDefinition = "LONGTEXT")
    private String crop_image;
    private String category;
    private String season;
    @ManyToOne
    @JoinColumn(name = "field_code")
    private FieldEntity field;
}
