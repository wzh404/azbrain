package com.blueocean.azbrain.model;

import lombok.Data;

import java.util.Date;

@Data
public class ConsultationLog {
    private Integer id;

    private Integer userId;

    private Integer userCid;

    private String way;

    private Date cdate;

    private String timeLabelCode;

    private Date startTime;

    private Date endTime;

    private String status;
}