package com.blueocean.azbrain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleEvaluate {
    private Integer id;
    private Integer userId;
    private Integer articleId;
    private String code;
    private String name;
    private String value;
    private LocalDateTime createTime;
}
