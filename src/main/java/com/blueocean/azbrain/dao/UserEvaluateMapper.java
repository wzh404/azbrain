package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.UserEvaluate;
import com.github.pagehelper.Page;
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

    /**
     * 评价的我所有用户
     *
     * @param byUserId
     * @return
     */
    Page<Map<String, Object>> evaluateOnUser(@Param("byUserId")Integer byUserId);

    /**
     * 用户评价汇总
     *
     * @param flag
     * @return
     */
    Page<Map<String, Object>> summaryUserEvaluation(@Param("flag")Integer flag);
}