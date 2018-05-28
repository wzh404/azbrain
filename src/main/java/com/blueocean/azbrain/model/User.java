package com.blueocean.azbrain.model;

import com.blueocean.azbrain.common.status.UserStatus;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * Created by @author wangzunhui on 2018/3/13.
 */
@Data
public class User extends Creator {
    private Integer id;
    private String name;
    private String loginName;
    private String wxid;
    @JsonIgnore
    private String password;
    private Integer companyId;
    private String jobNumber;
    private String userType;
    private String photo;
    private String email;
    private String mobile;
    private String status;
    private String inviteCode;

    public User(){
    }

    public User(String mobile, String wxid, int companyId){
        this.name = mobile;
        this.wxid = wxid;
        this.companyId = companyId;
        this.loginName = mobile;
        this.password = "";
        this.jobNumber = "";
        this.userType = "";
        this.photo = AZBrainConstants.DEFAULT_USER_PHOTO;
        this.email = "";
        this.mobile = mobile;
        this.status = UserStatus.NORMAL.getCode();
        this.createBy = 0;
        this.createTime = new Date();
        this.deleteFlag = "F";
        this.remarks = "";
    }

    public UserStatus status(){
        return UserStatus.get(this.status);
    }

    public boolean normal(){
        return this.status() == UserStatus.NORMAL;
    }

    public boolean closed(){
        return this.status() == UserStatus.CLOSED;
    }

    public boolean deleted(){
        return this.status() == UserStatus.DELETED;
    }
}
