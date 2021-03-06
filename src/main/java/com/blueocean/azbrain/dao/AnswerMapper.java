package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.Answer;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnswerMapper {
    /**
     *
     * @param record
     * @return
     */
    int insert(Answer record);

    /**
     *
     * @param id
     * @return
     */
    Answer get(@Param("id") Integer id);

    /**
     * 根据问题ID列表回答
     *
     * @param questionId
     * @return
     */
    Page<Answer> listByQuestionId(@Param("questionId") Integer questionId, @Param("orderBy") String orderBy);

    /**
     * 点赞人数加一
     *
     * @param id
     */
    void incrementLiker(@Param("answerId") Integer id);

    /**
     * 点赞人数减一
     *
     * @param id
     */
    void decrementLiker(@Param("answerId") Integer id);

    /**
     * 关键字搜索
     *
     * @param key
     * @return
     */
    Page<Answer> search(@Param("key") String key);

    /**
     * 修改回答状态
     *
     * @param id
     */
    int changeStatus(@Param("answerId") Integer id);

    /**
     * 修改回答内容
     *
     * @param answerId
     * @return
     */
    int update(@Param("answerId") Integer answerId, @Param("content") String content);
}