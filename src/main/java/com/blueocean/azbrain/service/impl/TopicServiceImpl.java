package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.TopicMapper;
import com.blueocean.azbrain.model.Topic;
import com.blueocean.azbrain.service.TopicService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("topicService")
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicMapper topicMapper;

    @Override
    public Page<Topic> userFollowTopics(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return topicMapper.userFollowTopics(conditionMap);
    }
}
