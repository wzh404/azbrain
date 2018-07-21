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
     * @return
     */
    Page<Map<String, Object>> evaluateOnUser(Map<String, Object> conditionMap);

    /**
     * 用户评价汇总
     *
     * @return
     */
    Page<Map<String, Object>> summaryUserEvaluation(Map<String, Object> conditionMap);

    /**
     * 删除用户评价
     *
     * @param userId
     * @param byUserId
     * @return
     */
    int deleteUserEvaluate(@Param("userId")Integer userId, @Param("byUserId")Integer byUserId);
}