package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.common.status.QuestionStatus;
import com.blueocean.azbrain.dao.*;
import com.blueocean.azbrain.model.*;
import com.blueocean.azbrain.service.QuestionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService{
    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserFollowQuestionMapper userFollowQuestionMapper;

    @Autowired
    private UserRecommendQuestionMapper userRecommendQuestionMapper;

    @Override
    public Page<Question> getUserFollowQuestions(int page, int pageSize, Integer userId) {
        logger.info("----page is {}", page);
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
    public Page<Answer> getQuestionAnswers(int page, int pageSize, int questionId, String orderBy) {
        PageHelper.startPage(page, pageSize);
        return answerMapper.listByQuestionId(questionId, orderBy);
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=SQLException.class)
    public int insert(Question question, List<Answer> answers) throws SQLException {
        questionMapper.insert(question);
        for (Answer answer: answers){
            User user = userMapper.get(answer.getCreateBy());
            if (user == null){
                throw new SQLException("user is not exist");
            }
            answer.setCreateName(user.getName());
            answer.setQuestionId(question.getId());
            answerMapper.insert(answer);
        }
        return 1;
    }

    @Override
    public int close(int questionId) {
        return questionMapper.changeStatus(questionId, QuestionStatus.CLOSED.getCode());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int recommend(int questionId) {
        questionMapper.changeRecommendStatus(questionId, 1);
        UserRecommendQuestion recommendQuestion = new UserRecommendQuestion();
        recommendQuestion.setQuestionId(questionId);
        recommendQuestion.setUserId(0);
        recommendQuestion.setCreateTime(new Date());
        return userRecommendQuestionMapper.insert(recommendQuestion);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int unrecommend(int questionId) {
        questionMapper.changeRecommendStatus(questionId, 0);
        return userRecommendQuestionMapper.delete(questionId);
    }

    @Override
    public boolean isRecommended(int questionId) {
        return userRecommendQuestionMapper.get(questionId) == null ? false : true;
    }

    @Override
    public int update(Integer questionId, String title, String content, String icon) {
        return questionMapper.update(questionId, title, content, icon);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int update(Question question, List<Answer> answers) {
        questionMapper.update(question.getId(), question.getTitle(), question.getContent(), question.getIcon());
        for (Answer answer: answers){
            answerMapper.update(answer.getId(), answer.getContent());
        }
        return 1;
    }
}
