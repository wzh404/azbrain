package com.blueocean.azbrain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserFollowTopic {
    private Integer id;
    private Integer userId;
    private Integer topicId;
    private LocalDateTime followTime;
}
