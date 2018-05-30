package com.blueocean.azbrain.model;

import com.blueocean.azbrain.common.status.UserStatus;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * Created by @author wangzunhui on 2018/3/13.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    protected String businessUnit;
    protected String pinyin;

    public User(){
    }

    public User(String mobile, String kcode, int companyId, String pinyin, String businessUnit){
        this.name = mobile;
        this.wxid = wxid;
        this.companyId = companyId;
        this.loginName = kcode;
        this.password = "";
        this.jobNumber = kcode;
        this.userType = "";
        this.photo = AZBrainConstants.DEFAULT_USER_PHOTO;
        this.email = "";
        this.mobile = mobile;
        this.status = UserStatus.NORMAL.getCode();
        this.createBy = 0;
        this.createTime = new Date();
        this.businessUnit = businessUnit;
        this.pinyin = pinyin;
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
