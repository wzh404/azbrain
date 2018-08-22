package com.blueocean.azbrain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventLog {
    private Integer id;
    private String who;
    private String content;
    private String type;
    private Integer level;
    private Integer eventId;
    private Integer duration;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
}
