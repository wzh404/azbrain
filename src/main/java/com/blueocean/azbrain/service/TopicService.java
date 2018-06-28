package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.Topic;
import com.github.pagehelper.Page;

import java.util.Map;

public interface TopicService {
    Page<Topic> userFollowTopics(int page, int pageSize, Map<String, Object> conditionMap);
}
