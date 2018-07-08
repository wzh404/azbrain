package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.UserEvaluate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserEvaluateMapper {
    /**
     * 用户评价
     *
     * @param records
     * @return
     */
    int insertBatch(List<UserEvaluate> records);

    /**
     * 获取对用户评价
     *
     * @param id
     * @return
     */
    UserEvaluate get(Integer id);

    /**
     *
     * @param logId
     * @param userId
     * @return
     */
    List<UserEvaluate> getEvaluationByLog(@Param("logId") Integer logId, @Param("userId")Integer userId);

}