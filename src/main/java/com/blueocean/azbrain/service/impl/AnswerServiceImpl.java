package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.AnswerCommentMapper;
import com.blueocean.azbrain.dao.AnswerMapper;
import com.blueocean.azbrain.dao.UserLikeAnswerMapper;
import com.blueocean.azbrain.model.Answer;
import com.blueocean.azbrain.model.AnswerComment;
import com.blueocean.azbrain.service.AnswerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private AnswerCommentMapper answerCommentMapper;

    @Autowired
    private UserLikeAnswerMapper userLikeAnswerMapper;

    @Override
    public Answer get(int id) {
        return answerMapper.get(id);
    }

    @Override
    public Page<AnswerComment> getAnswerComments(int page, int pageSize, int answerId) {
        PageHelper.startPage(page, pageSize);
        return answerCommentMapper.getAnswerComments(answerId);
    }

    @Override
    public Page<Answer> search(int page, int pageSize, String key) {
        PageHelper.startPage(page, pageSize);
        return answerMapper.search(key);
    }

    @Override
    public boolean isLiked(int userId, int answerId) {
        return userLikeAnswerMapper.getUserLikeAnswer(userId, answerId) == null ? false : true;
    }

    @Override
    public int insert(Answer answer) {
        return answerMapper.insert(answer);
    }
}
