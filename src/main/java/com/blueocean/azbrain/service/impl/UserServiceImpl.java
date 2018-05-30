package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.*;
import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.model.UserFollowQuestion;
import com.blueocean.azbrain.model.UserInviteCode;
import com.blueocean.azbrain.model.UserLikeAnswer;
import com.blueocean.azbrain.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by @author wangzunhui on 2018/3/13.
 */
@Service("userService")
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserFollowQuestionMapper userFollowQuestionMapper;

    @Autowired
    private UserLikeAnswerMapper userLikeAnswerMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private UserInviteCodeMapper inviteCodeMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int insert(User user, String inviteCode) {
        UserInviteCode userInviteCode = inviteCodeMapper.getByInviteCode(inviteCode);
        if (userInviteCode == null || !userInviteCode.isValid()){
            return 0;
        }

        user.setInviteCode(inviteCode);
        userMapper.insert(user);
        return inviteCodeMapper.receive(user.getId(), inviteCode, new Date());
    }

    @Override
    public int delete(Integer id) {
        return userMapper.delete(id);
    }

    @Override
    public User get(Integer id) {
        return userMapper.get(id);
    }

    @Override
    public User getUserByName(String login) {
        return userMapper.getUserByName(login);
    }

    @Override
    public Page<User> findByPage(int page, int pageSize, HashMap<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return userMapper.findByPage(conditionMap);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int follow(int userId, int questionId) {
        UserFollowQuestion ufq  = userFollowQuestionMapper.getUserFollowQuestion(userId, questionId);
        if (ufq != null){
            // 已关注
            return -1;
        }

        ufq = new UserFollowQuestion();
        ufq.setUserId(userId);
        ufq.setQuestionId(questionId);
        ufq.setFollowTime(new Date());

        // uid & quesiton_id设置为唯一索引
        userFollowQuestionMapper.insert(ufq);
        questionMapper.incrementFollower(questionId);

        return 0;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int unfollow(int userId, int questionId) {
        UserFollowQuestion ufq = userFollowQuestionMapper.getUserFollowQuestion(userId, questionId);
        if (ufq == null || ufq.getId() == null){
            return -1;
        }

        userFollowQuestionMapper.delete(ufq.getId());
        questionMapper.decrementFollower(ufq.getQuestionId());

        return 0;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int like(int userId, int answerId) {
        UserLikeAnswer ula = userLikeAnswerMapper.getUserLikeAnswer(userId, answerId);
        if (ula != null){
            // 已点赞
            return -1;
        }

        ula = new UserLikeAnswer();
        ula.setAnswerId(answerId);
        ula.setUserId(userId);
        ula.setCreateTime(new Date());

        userLikeAnswerMapper.insert(ula);
        answerMapper.incrementLiker(answerId);

        return 0;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int unlike(int userId, int answerId) {
        UserLikeAnswer ula = userLikeAnswerMapper.getUserLikeAnswer(userId, answerId);
        if (ula == null){
            return -1;
        }
        userLikeAnswerMapper.delete(ula.getId());
        answerMapper.decrementLiker(ula.getAnswerId());

        return 0;
    }

    @Override
    public int changeStatus(int id, String status) {
        return userMapper.changeStatus(id, status);
    }

    @Override
    public List<User> list(){
        return userMapper.list();
    }
}
