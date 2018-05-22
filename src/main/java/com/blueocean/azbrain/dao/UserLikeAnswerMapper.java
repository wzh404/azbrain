package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.UserLikeAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLikeAnswerMapper {
    int delete(@Param("id") Integer id);

    /**
     *
     * @param id
     * @return
     */
    UserLikeAnswer get(@Param("id") Integer id);

    /**
     *
     * @param record
     * @return
     */
    int insert(UserLikeAnswer record);

    /**
     *
     * @param userId
     * @param answerId
     * @return
     */
    UserLikeAnswer getUserLikeAnswer(@Param("userId") Integer userId, @Param("answerId") Integer answerId);
}