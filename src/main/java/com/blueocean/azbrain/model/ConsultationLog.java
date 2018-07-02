package com.blueocean.azbrain.model;

import com.blueocean.azbrain.common.status.ConsultationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Data
public class ConsultationLog {
    private Integer id;

    // 咨询人
    private Integer userId;

    // 被咨询人
    private Integer byUserId;

    // 咨询的主题
    private Integer topicId;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastUpdated;

    private Integer userCommentFlag;

    private Integer specialistCommentFlag;

    private String mobile;

    private String meetingPwd;

    private String status;

    public Long duration(){
        return ChronoUnit.MINUTES.between(startTime, endTime);
    }

    /**
     * 咨询时间是否结束，可以评价
     *
     * @return
     */
    public boolean isExpire(){
        return LocalDateTime.of(cdate, endTime).isAfter(LocalDateTime.now());
    }

    @JsonIgnore
    public boolean isConfirmed(){
        return status.equalsIgnoreCase(ConsultationStatus.CONFIRMED.getCode());
    }

    @JsonIgnore
    public boolean isApplied(){
        return status.equalsIgnoreCase(ConsultationStatus.APPLIED.getCode());
    }

    @JsonIgnore
    public boolean isSpecialist(int userId){
        return this.byUserId.intValue() == userId;
    }

    @JsonIgnore
    public boolean isConsultant(int userId){
        return this.userId.intValue() == userId;
    }
}