package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.*;
import com.blueocean.azbrain.vo.ConsultationLogVo;
import com.blueocean.azbrain.vo.UserEvaluateVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ConsultationService {
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

    /**
     * change consultation log status.
     *
     * @param id
     * @param status
     * @return
     */
    int changeStatus(Integer id, String status);

    /**
     * confirm consultation status
     *
     * @param consultationLog
     * @return
     */
    int confirm(ConsultationLog consultationLog);

    /**
     * 用户或专家编辑
     *
     * @param consultationLogVo
     * @return
     */
    int edit(ConsultationLogVo consultationLogVo);


    /**
     * insert user evaluate
     *
     * @param record
     * @return
     */
    int insertUserEvaluate(UserEvaluateVo record);
    /**
     * 我的咨询
     *
     * @param userId
     * @return
     */
    Page<ConsultationLog> myConsult(int page, int pageSize, Integer userId);

    /**
     * 咨询我的
     *
     * @param userId
     * @return
     */
    Page<ConsultationLog> consultMe(int page, int pageSize, Integer userId);

    // manager
    /**
     *
     * @param conditionMap
     * @return
     */
    Page<ConsultationLog> listConsultation(int page, int pageSize, Map<String, Object> conditionMap);

    /**
     *
     * @param logId
     * @return
     */
    Map<String, Object> getEvaluationByLog(Integer logId);
}
