package com.blueocean.azbrain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserScoreLog {
    private Integer id;

    private Integer logId;

    //评价人 -> 专家
    private Integer userId;

    //被评价人 -> 用户
    private Integer byUserId;

    // 爽约
    private Integer breakContract;

    // 态度
    private Integer attitude;

    // 礼貌
    private Integer polite;

    // 准时
    private Integer punctuality;

    private String other;

    private LocalDateTime createTime;
}