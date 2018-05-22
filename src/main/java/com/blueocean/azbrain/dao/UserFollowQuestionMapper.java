package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.UserFollowQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserFollowQuestionMapper {
    int delete(@Param("id")Integer id);

    int insert(UserFollowQuestion record);

    UserFollowQuestion get(@Param("id")Integer id);

    UserFollowQuestion getUserFollowQuestion(@Param("userId")Integer userId, @Param("questionId")Integer questionId);
}