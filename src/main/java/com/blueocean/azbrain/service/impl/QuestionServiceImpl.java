package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.common.QuestionStatus;
import com.blueocean.azbrain.dao.AnswerMapper;
import com.blueocean.azbrain.dao.QuestionMapper;
import com.blueocean.azbrain.dao.UserFollowQuestionMapper;
import com.blueocean.azbrain.model.Answer;
import com.blueocean.azbrain.model.Question;
import com.blueocean.azbrain.model.UserFollowQuestion;
import com.blueocean.azbrain.service.QuestionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService{
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private UserFollowQuestionMapper userFollowQuestionMapper;

    @Override
    public Page<Question> getUserFollowQuestions(int page, int pageSize, Integer userId) {
        PageHelper.startPage(page, pageSize);
        return questionMapper.getUserFollowQuestions(userId);
    }

    @Override
    public Page<Question> getUserRecommendQuestions(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return questionMapper.getUserRecommendQuestions();
    }

    @Override
    public Question get(int id) {
        return questionMapper.get(id);
    }

    @Override
    public Page<Answer> getQuestionAnswers(int page, int pageSize, int questionId) {
        PageHelper.startPage(page, pageSize);
        return answerMapper.listByQuestionId(questionId);
    }

    @Override
    public Page<Question> search(int page, int pageSize, String key) {
        PageHelper.startPage(page, pageSize);
        return questionMapper.search(key);
    }

    @Override
    public boolean isFollowed(int userId, int questionId) {
        UserFollowQuestion ufq = userFollowQuestionMapper.getUserFollowQuestion(userId, questionId);
        return ufq == null ? false : true;
    }

    @Override
    public Page<Question> searchByCondition(int page, int pageSize, HashMap<String, Object> map) {
        PageHelper.startPage(page, pageSize);
        return questionMapper.searchByCondition(map);
    }

    @Override
    public int insert(Question question) {
        return questionMapper.insert(question);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int insert(Question question, List<Answer> answers) {
        int ret = questionMapper.insert(question);
        for (Answer answer: answers){
            answer.setQuestionId(question.getId());
            answerMapper.insert(answer);
        }
        return ret;
    }

    @Override
    public int close(int questionId) {
        return questionMapper.changeStatus(questionId, QuestionStatus.CLOSED.getCode());
    }
}
