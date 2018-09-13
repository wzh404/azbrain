package com.blueocean.azbrain.model;

import com.blueocean.azbrain.util.ExcelResources;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventLog {
    @ExcelResources(title="ID", order=1)
    private Integer id;

    @ExcelResources(title="用户", order=2)
    private String who;

    @ExcelResources(title="事件名称", order=3)
    private String content;

    @ExcelResources(title="事件类型", order=4)
    private String type;
    private Integer level;

    @ExcelResources(title="事件关联ID", order=5)
    private Integer eventId;

    @ExcelResources(title="事件耗时ms", order=6)
    private Integer duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @ExcelResources(title="事件发生时间", order=7)
    private LocalDateTime createTime;

    @ExcelResources(title="事件描述", order=8)
    private String remark;
}
