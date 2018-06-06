package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.Answer;
import com.blueocean.azbrain.model.AnswerComment;
import com.github.pagehelper.Page;


public interface AnswerService {
    /**
     * 获取回答信息
     *
     * @param id
     * @return
     */
    Answer get(int id);

    /**
     * 获取回答的评论
     *
     * @param page
     * @param pageSize
     * @param answerId
     * @return
     */
    Page<AnswerComment> getAnswerComments(int page, int pageSize, int answerId);

    /**
     * 搜索回答
     *
     * @param page
     * @param pageSize
     * @param key
     * @return
     */
    Page<Answer> search(int page, int pageSize, String key);

    /**
     * 是否点赞过
     *
     * @param userId
     * @param answerId
     * @return
     */
    boolean isLiked(int userId, int answerId);

    /**
     * 新增回答
     *
     * @param answer
     * @return
     */
    int insert(Answer answer);

    /**
     * 修改回答内容
     *
     * @param answerId
     * @param content
     * @return
     */
    int update(Integer answerId, String content);
}
