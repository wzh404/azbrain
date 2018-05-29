package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.UserRecommendQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRecommendQuestionMapper {
    /**
     * 取消推荐
     *
     * @param questionId
     * @return
     */
    int delete(@Param("questionId") Integer questionId);

    /**
     * 推荐问题
     *
     * @param record
     * @return
     */
    int insert(UserRecommendQuestion record);

    /**
     * 获取推荐问题
     *
     * @param questionId
     * @return
     */
    UserRecommendQuestion get(@Param("questionId")Integer questionId);
}