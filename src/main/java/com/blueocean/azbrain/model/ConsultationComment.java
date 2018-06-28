package com.blueocean.azbrain.model;

import lombok.Data;

@Data
public class ConsultationComment {
    private Integer id;

    private Integer logId;

    private Integer breakContract;

    private Integer expertiseLevel;

    private Integer attitudeLevel;

    private Integer patienceLevel;

    private Integer articulation;

    private Integer logicalClarity;

    private Integer punctuality;

    private String other;
}