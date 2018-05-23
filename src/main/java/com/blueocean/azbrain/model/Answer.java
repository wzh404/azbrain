package com.blueocean.azbrain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Answer {
    private Integer id;

    @JsonProperty("follow_num")
    private Integer questionId;

    private String content;

    @JsonProperty("like_num")
    private Integer likeNum;

    @JsonProperty("view_num")
    private Integer viewNum;

    @JsonProperty("comment_num")
    private Integer commentNum;

    @JsonProperty("create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    @JsonProperty("create_by")
    private Integer createBy;

    private String status;

    @JsonProperty("create_name")
    private String createName;
}