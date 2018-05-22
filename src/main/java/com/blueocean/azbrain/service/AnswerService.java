package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.Answer;
import com.blueocean.azbrain.model.AnswerComment;
import com.github.pagehelper.Page;


public interface AnswerService {
    Answer get(int id);

    Page<AnswerComment> getAnswerComments(int page, int pageSize, int answerId);

    Page<Answer> search(int page, int pageSize, String key);

    /**
     * 是否点赞过
     *
     * @param userId
     * @param answerId
     * @return
     */
    boolean isLiked(int userId, int answerId);
}
