package com.example.greenshadow.entity.impl;

import com.example.greenshadow.entity.SuperEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Field")
@ToString(exclude = {"crops", "allocated_staff"})
public class FieldEntity implements SuperEntity {

    @Id
    private String field_code;
    private String field_name;
    private Point location;
    private Double extent_size;
    @Column(columnDefinition = "LONGTEXT")
    private String field_image1;
    @Column(columnDefinition = "LONGTEXT")
    private String field_image2;
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    private java.util.List<CropEntity> crops = new ArrayList<>();
    @ManyToMany(mappedBy = "fields")
    private List<StaffEntity> allocated_staff = new ArrayList<>();
}
