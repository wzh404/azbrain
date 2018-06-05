package com.blueocean.azbrain.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 统计模型
 *
 */
@Mapper
public interface StatisticsMapper {
    Integer totalQuestion(Map<String, Object> conditionMap);

    Integer totalAnswer(Map<String, Object> conditionMap);

    Integer totalLike(Map<String, Object> conditionMap);
}
