package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.*;
import com.blueocean.azbrain.vo.ConsultationLogVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

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
     * @param id
     * @param topicId
     * @return
     */
    int confirm(Integer id, Integer topicId);

    /**
     * 用户或专家编辑
     *
     * @param consultationLogVo
     * @return
     */
    int edit(ConsultationLogVo consultationLogVo);

    /**
     * user's score log
     *
     * @param record
     * @return
     */
    int insertUserScoreLog(UserScoreLog record);

    /**
     * specialist's score log
     *
     * @param record
     * @return
     */
    int insertSpecialistScoreLog(SpecialistScoreLog record);

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
}
