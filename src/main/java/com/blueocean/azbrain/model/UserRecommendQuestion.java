package com.blueocean.azbrain.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserRecommendQuestion {
    private Integer id;

    private Integer userId;

    private Integer questionId;

    private Date createTime;
}