package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.UserInviteCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface UserInviteCodeMapper {
    int insert(UserInviteCode record);

    UserInviteCode get(@Param("id")Integer id);

    UserInviteCode getByInviteCode(@Param("inviteCode")String inviteCode);

    int receive(@Param("userId")Integer userId, @Param("inviteCode")String inviteCode, @Param("receiveTime")Date receiveTime);
}