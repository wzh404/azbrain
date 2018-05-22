package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.UserInviteCode;
import com.blueocean.azbrain.model.UserRecommendQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface UserRecommendQuestionMapper {
    int delete(@Param("id") Integer id);

    /**
     * 生成邀请码
     *
     * @param record
     * @return
     */
    int insert(UserRecommendQuestion record);

    /**
     * 领取邀请码
     *
     * @param userId
     * @param createTime
     *
     * @return
     */
    int complete(@Param("inviteCode")String inviteCode, @Param("userId")Integer userId, @Param("createTime")Date createTime);

    /**
     *
     * @param inviteCode
     * @return
     */
    UserInviteCode getByInviteCode(@Param("inviteCode") String inviteCode);

    /**
     *
     * @param id
     * @return
     */
    UserInviteCode get(@Param("id") Integer id);
}