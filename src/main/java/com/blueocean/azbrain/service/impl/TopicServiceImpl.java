package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.ArticleMapper;
import com.blueocean.azbrain.dao.TopicMapper;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.Topic;
import com.blueocean.azbrain.model.UserFollowTopic;
import com.blueocean.azbrain.service.TopicService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service("topicService")
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public Topic get(int id) {
        return topicMapper.get(id);
    }

    @Override
    public Page<Topic> myFollowTopics(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return topicMapper.userFollowTopics(conditionMap);
    }

    @Override
    public Page<Article> listArticlesByTopic(int page, int pageSize, Integer topicId) {
        PageHelper.startPage(page, pageSize);
        return articleMapper.listByTopic(topicId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int follow(UserFollowTopic followTopic) {
        return topicMapper.follow(followTopic);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int unfollow(Integer userId, Integer topicId) {
        return topicMapper.unfollow(userId, topicId);
    }

    @Override
    public boolean isFollowed(Integer userId, Integer topicId) {
        return topicMapper.followNum(userId, topicId) > 0 ? true : false;
    }

    @Override
    public Page<Topic> pageTopics(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return topicMapper.pageTopics(conditionMap);
    }

    @Override
    public int newTopic(Topic topic) {
        return topicMapper.insert(topic);
    }

    @Override
    public int editTopic(Topic topic) {
        return topicMapper.edit(topic);
    }

    @Override
    public Topic viewTopic(Integer topicId) {
        return topicMapper.get(topicId);
    }

    @Override
    public int newTopicSpecialist(Integer topicId, Integer userId) {
        return topicMapper.insertTopicSpecialist(topicId, userId);
    }

    @Override
    public int deleteTopicSpecialist(Integer topicId, Integer userId) {
        return topicMapper.deleteTopicSpecialist(topicId, userId);
    }
}
