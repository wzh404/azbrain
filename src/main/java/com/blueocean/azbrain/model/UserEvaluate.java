package com.blueocean.azbrain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserEvaluate {
    private Integer id;
    private Integer userId;
    private Integer byUserId;
    private Integer logId;
    private String code;
    private String name;
    private String value;
    private LocalDateTime createTime;
}
