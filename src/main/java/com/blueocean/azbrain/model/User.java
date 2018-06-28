package com.blueocean.azbrain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private Integer id;

    private String name;

    private String loginName;

    @JsonIgnore
    private String password;

    private String jobNumber;

    private String userType;

    private String photo;

    private String email;

    private String mobile;

    private String remarks;

    private Integer likesNum;

    private Integer favoriteNum;

    private Integer badNum;
    private Integer goodNum;

    private Integer consultedNum;
    private Integer consultedDuration;

    private Integer breakContractNum;

    private Integer consultationDuration;

    private Integer createBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    private String status;
}