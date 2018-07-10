package com.blueocean.azbrain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEvaluate {
    private Integer id;
    private Integer userId;
    private Integer byUserId;
    private Integer byUserFlag;
    private Integer logId;
    private String code;
    private String name;
    private String value;
    private String valueType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
}
