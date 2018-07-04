package com.blueocean.azbrain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Article {
    private Integer id;

    private String title;

    private String content;

    private String icon;

    private Integer likeNum;

    private Integer favoriteNum;

    private Integer topicId;

    // 平台， 专家， 转载
    private String source;

    private String author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    private Integer createBy;

    private String status;
}