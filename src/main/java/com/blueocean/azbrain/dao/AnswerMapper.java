package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.Answer;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnswerMapper {
    int insert(Answer record);

    Answer get(@Param("id")Integer id);

    Page<Answer> listByQuestionId(@Param("questionId")Integer questionId);

    /**
     * 点赞人数加一
     *
     * @param id
     */
    void incrementLiker(@Param("answerId")Integer id);

    /**
     * 点赞人数减一
     *
     * @param id
     */
    void decrementLiker(@Param("answerId")Integer id);

    /**
     * 关键字搜索
     *
     * @param key
     * @return
     */
    Page<Answer> search(@Param("key")String key);
}