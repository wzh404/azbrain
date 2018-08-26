package com.blueocean.azbrain.model;

import com.blueocean.azbrain.util.ExcelResources;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Article {
    @ExcelResources(title="ID", order = 1)
    private Integer id;

    @ExcelResources(title="标题", order = 2)
    private String title;

    private String content;

    private String icon;

    private Integer likeNum;

    private Integer favoriteNum;

    private Integer topicId;

    private Integer topFlag;

    // 平台， 专家， 转载
    private String source;

    private String sourceUrl;

    private String sourceSite;

    private String author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    private Integer createBy;

    private String status;

    private String topicTitle;

    @ExcelResources(title="点击数", order = 3)
    private Long clickedNum;
}