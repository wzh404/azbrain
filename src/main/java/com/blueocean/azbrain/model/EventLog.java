package com.blueocean.azbrain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventLog {
    private Integer id;
    private String who;
    private String content;
    private String type;
    private Integer level;
    private LocalDateTime createTime;
}
