package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.UserScoreLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserScoreLogMapper {
    /**
     * 评价咨询人
     *
     * @param record
     * @return
     */
    int insert(UserScoreLog record);

    /**
     * 获取对咨询人的评价
     *
     * @param id
     * @return
     */
    UserScoreLog get(Integer id);
}