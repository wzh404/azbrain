package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.Answer;
import com.blueocean.azbrain.model.Question;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

public interface QuestionService {
    /**
     * 用户关注的问题
     *
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    Page<Question> getUserFollowQuestions(int page, int pageSize, Integer userId);

    /**
     * 给用户推荐的问题
     *
     * @param page
     * @param pageSize
     * @return
     */
    Page<Question> getUserRecommendQuestions(int page, int pageSize);
    /**
     *
     * @param id
     * @return
     */
    Question get(int id);

    /**
     * 问题答案列表
     *
     * @param page
     * @param pageSize
     * @param questionId
     * @return
     */
    Page<Answer> getQuestionAnswers(int page, int pageSize, int questionId);

    /**
     *
     * @param page
     * @param pageSize
     * @param key
     * @return
     */
    Page<Question> search(int page, int pageSize, String key);

    /**
     * 用户是否已关注改问题
     *
     * @param userId
     * @param questionId
     * @return
     */
    boolean isFollowed(int userId, int questionId);
}
