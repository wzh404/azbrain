package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.ConsultationLog;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    int setSpecialistCommented(@Param("id")Integer id);

    /**
     * 设置用户已评论
     *
     * @param id
     * @return
     */
    int setUserCommented(@Param("id")Integer id);

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
}