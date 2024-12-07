package com.example.greenshadow.dao;

import com.example.greenshadow.entity.impl.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FieldDao extends JpaRepository<FieldEntity,String> {

    @Query("SELECT f FROM FieldEntity f WHERE f.field_name = :field_name")
    Optional<FieldEntity> findByFieldName(@Param("field_name") String field_name);

    @Query("SELECT f FROM FieldEntity f WHERE f.field_name IN :field_name")
    List<FieldEntity> findByFieldNameList(@Param("field_name") List<String> field_name);
}
