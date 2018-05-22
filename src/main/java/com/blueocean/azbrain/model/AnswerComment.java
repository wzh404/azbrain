package com.blueocean.azbrain.model;

import lombok.Data;

import java.util.Date;

@Data
public class AnswerComment {
    private Integer id;

    private Integer questionId;

    private Integer answerId;

    private String content;

    private Integer likeNum;

    private Integer createId;

    private Date createTime;

    private Integer replyCommentId;

    private Integer replyUserId;

    private String status;
}