package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.UserFeedback;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;

@Mapper
public interface UserFeedbackMapper {
    /**
     * 新增反馈意见
     *
     * @param feedback
     * @return
     */
    int insert(UserFeedback feedback);

    /**
     * 查看反馈意见
     *
     * @param id
     * @return
     */
    UserFeedback get(@Param("id")int id);

    /**
     * 分页查询反馈意见
     *
     * @param conditionMap
     * @return
     */
    Page<UserFeedback> list(HashMap<String, Object> conditionMap);
}
