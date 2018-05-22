package com.blueocean.azbrain.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserLikeAnswer {
    private Integer id;

    private Integer userId;

    private Integer answerId;

    private Date createTime;
}