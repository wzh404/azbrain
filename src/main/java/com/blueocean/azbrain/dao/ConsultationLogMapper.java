package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.ConsultationLog;
import com.blueocean.azbrain.model.UserEvaluate;
import com.blueocean.azbrain.vo.ConsultationLogVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

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

    /**
     *
     * @param id
     * @param status
     * @return
     */
    int changeStatus(@Param("id")Integer id, @Param("status")String status);

    /**
     * 设置专家已评论
     *
     * @param id
     * @return
     */
    int byUserEvaluated(@Param("id")Integer id);

    /**
     * 设置用户已评论
     *
     * @param id
     * @return
     */
    int userEvaluated(@Param("id")Integer id);

    /**
     * 我的咨询
     *
     * @param userId
     * @return
     */
    Page<ConsultationLog> myConsult(@Param("userId")Integer userId);

    /**
     * 咨询我的
     *
     * @param userId
     * @return
     */
    Page<ConsultationLog> consultMe(@Param("userId")Integer userId);

    /**
     * 编辑
     *
     * @param consultationLogVo
     * @return
     */
    int edit(ConsultationLogVo consultationLogVo);

    /**
     *
     * @param conditionMap
     * @return
     */
    Page<ConsultationLog> listConsultation(Map<String, Object> conditionMap);

    /**
     *
     * @return
     */
    List<ConsultationLog> selectHostAndPwd();

    /**
     *
     * @return
     */
    List<Map<String, String>>listMeeting();

    List<ConsultationLog> listReminderLogs();

    int selectSametimeLog(@Param("userId") Integer userId,
                          @Param("cdate")LocalDate cdate,
                          @Param("startTime")LocalTime startTime,
                          @Param("endTime")LocalTime endTime);

    int selectBySametimeLog(@Param("byUserId") Integer byUserId,
                          @Param("cdate")LocalDate cdate,
                          @Param("startTime")LocalTime startTime,
                          @Param("endTime")LocalTime endTime);
}