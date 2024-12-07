package com.example.greenshadow.entity.impl;

import com.example.greenshadow.entity.EquipmentType;
import com.example.greenshadow.entity.Status;
import com.example.greenshadow.entity.SuperEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "equipment")
public class EquipmentEntity implements SuperEntity {

    @Id
    private String equipment_id;
    private String name;
    @Enumerated(EnumType.STRING)
    private EquipmentType type;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id",nullable = true)
    private StaffEntity assigned_staff;
    @ManyToOne
    @JoinColumn(name = "field_code")
    private FieldEntity assigned_field;
}
