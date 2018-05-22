package com.blueocean.azbrain.model;

import lombok.Data;

import java.util.Date;

@Data
public class Topic {
    private Integer id;

    private String title;

    private String content;

    private String icon;

    private Integer followerNum;

    private Date createTime;

    private Integer createBy;

    private String status;
}