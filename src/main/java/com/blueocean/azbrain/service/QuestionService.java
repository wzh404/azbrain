package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.Answer;
import com.blueocean.azbrain.model.Question;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

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

    /**
     * 按条件查询问题
     *
     * @param map
     * @return
     */
    Page<Question> searchByCondition(int page, int pageSize, HashMap<String, Object> map);

    /**
     * 新增问题
     *
     * @param question
     * @return
     */
    int insert(Question question);

    /**
     * 新增问题及其回答
     *
     * @param question
     * @param answers
     * @return
     */
    int insert(Question question, List<Answer> answers);

    /**
     * 关闭问题
     *
     * @param questionId
     * @return
     */
    int close(int questionId);
}
