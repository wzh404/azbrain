package com.blueocean.azbrain.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface StatisticsMapper {
    int statArticle(@Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);
    int statTopic(@Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);
    int statConsultation(@Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);
    int statLikes(@Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);
    int statFollows(@Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);
}
