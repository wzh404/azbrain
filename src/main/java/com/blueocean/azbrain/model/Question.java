package com.blueocean.azbrain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.beans.Transient;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question{
    private Integer id;
    private String title;
    private String content;
    private String icon;
    @JsonProperty("follow_num")
    private Integer followerNum;
    @JsonProperty("answer_num")
    private Integer answerNum;

    @JsonProperty("create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;
    @JsonProperty("create_by")
    private Integer createBy;
    private String status;
    @JsonProperty("create_name")
    private String createName;

    private Integer recommend;
}