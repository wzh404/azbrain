package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.ConsultationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConsultationLogMapper {

    int insert(ConsultationLog record);


    ConsultationLog get(Integer id);
}