package com.blueocean.azbrain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpecialistScoreLog {
    private Integer id;

    private Integer logId;

    // 评价人 -> 咨询人员
    private Integer userId;

    // 被评价人 -> 专家
    private Integer byUserId;

    // 爽约
    private Integer breakContract;

    // 专业
    private Integer specialization;

    // 态度
    private Integer attitude;

    // 耐心
    private Integer patience;

    // 口齿清晰
    private Integer articulation;

    // 逻辑
    private Integer logicalClarity;

    // 准时
    private Integer punctuality;

    private String other;

    private LocalDateTime createTime;
}