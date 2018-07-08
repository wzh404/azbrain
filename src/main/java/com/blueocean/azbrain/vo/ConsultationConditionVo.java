package com.blueocean.azbrain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsultationConditionVo {
    private String status;
    private String userName;
    private String byUserName;
    private String way;
}
