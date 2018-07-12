package com.blueocean.azbrain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleEvaluate {
    private Integer id;
    private Integer userId;
    private Integer articleId;
    private String code;
    private String name;
    private String value;
    private LocalDateTime createTime;
}
