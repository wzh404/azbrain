package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.Topic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TopicMapper {
    int insert(Topic record);

    Topic get(@Param("id")Integer id);
}