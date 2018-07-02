package com.blueocean.azbrain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserLikeArticle {
    private Integer id;
    private Integer userId;
    private Integer articleId;
    private LocalDateTime likeTime;
}
