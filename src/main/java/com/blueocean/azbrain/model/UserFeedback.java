package com.blueocean.azbrain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户反馈意见
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFeedback {
    private Integer id;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("user_name")
    private String userName;
    private String feedback;
    @JsonProperty("create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    public UserFeedback(){}

    public UserFeedback(Integer userId, String userName, String feedback){
        this.userId = userId;
        this.createTime = new Date();
        this.userName = userName;
        this.feedback = feedback;
    }
}
