package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.Question;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuestionMapper {
    /**
     *
     * @param record
     * @return
     */
    int insert(Question record);

    /**
     *
     * @param id
     * @return
     */
    Question get(@Param("id")Integer id);

    /**
     * 关注的问题
     *
     * @param userId
     * @return
     */
    Page<Question> getUserFollowQuestions(@Param("userId")Integer userId);

    /**
     * 推荐的问题
     *
     * @return
     */
    Page<Question>getUserRecommendQuestions();

    /**
     * 关注人数加一
     *
     * @param id
     */
    void incrementFollower(@Param("questionId")Integer id);

    /**
     * 关注人数减一
     *
     * @param id
     */
    void decrementFollower(@Param("questionId")Integer id);

    /**
     * 关键字搜索问题
     *
     * @param key
     * @return
     */
    Page<Question> search(@Param("key")String key);
}