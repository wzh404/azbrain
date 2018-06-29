package com.blueocean.azbrain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ConsultationLog {
    private Integer id;

    // 咨询人
    private Integer userId;

    // 被咨询人
    private Integer byUserId;

    // 咨询方式
    private String way;

    // 咨询日期
    private LocalDate cdate;

    // 咨询周
    private Integer week;

    // 咨询时间标签代码
    private String code;

    // 预约开始时间
    private LocalTime startTime;

    // 预约结束时间
    private LocalTime endTime;

    // 创建时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    private String status;
}