package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.ConsultationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConsultationLogMapper {
    /**
     *
     * @param record
     * @return
     */
    int insert(ConsultationLog record);

    /**
     *
     * @param id
     * @return
     */
    ConsultationLog get(Integer id);
}