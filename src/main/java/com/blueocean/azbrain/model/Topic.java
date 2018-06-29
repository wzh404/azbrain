package com.blueocean.azbrain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Topic {
    private Integer id;

    private String title;

    private String content;

    private String icon;

    private Integer followerNum;

    private Integer consultedNum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    private Integer createBy;

    private String status;

}