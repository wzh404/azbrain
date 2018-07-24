package com.blueocean.azbrain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Topic {
    private Integer id;

    private String title;

    private String content;

    private String icon;

    private Integer followerNum;

    private Integer consultedNum;

    private Integer specialistNum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    private Integer createBy;

    private String status;

    private Integer updatedArticleNum;

    private List<Integer> uids;
}