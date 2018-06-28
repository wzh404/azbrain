package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.Topic;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface TopicMapper {
    int insert(Topic record);
    Topic get(Integer id);

    /**
     *
     * @param conditionMap
     * @return
     */
    Page<Topic> userFollowTopics(Map<String, Object> conditionMap);
}