package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.SpecialistScoreLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpecialistScoreLogMapper {
    /**
     * 评价被咨询的专家
     *
     * @param record
     * @return
     */
    int insert(SpecialistScoreLog record);

    /**
     * 获取对专家的评价
     * 
     * @param id
     * @return
     */
    SpecialistScoreLog get(Integer id);
}