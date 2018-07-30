package com.blueocean.azbrain.model;

import com.blueocean.azbrain.common.status.ConsultationStatus;
import com.blueocean.azbrain.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    // 预约结束时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    // 创建时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastUpdated;

    private Integer userEvaluated;

    private Integer byUserEvaluated;

    private String mobile;

    private String meetingPwd;

    private String meetingHost;

    private String status;

    private String userName;

    private String byUserName;

    public String getUserName(){
        if (this.userName == null || useRealName) {
            return this.userName;
        }
        return StringUtil.toStar(this.userName);
    }

    public String getByUserName(){
        if (this.byUserName == null || useRealName) {
            return this.byUserName;
        }
        return StringUtil.toStar(this.byUserName);
    }

    @JsonIgnore
    public boolean useRealName = true;

    @JsonProperty("duration")
    public Long duration(){
        return ChronoUnit.MINUTES.between(startTime, endTime);
    }

    /**
     * 咨询时间是否结束，可以评价
     *
     * @return
     */
    @JsonProperty("expired")
    public boolean expired(){
        return LocalDateTime.of(cdate, endTime).isBefore(LocalDateTime.now());
    }

    @JsonIgnore
    public boolean confirmed(){
        return cond(ConsultationStatus.CONFIRMED);
    }

    @JsonIgnore
    public boolean unconfirmed(){
        return cond(ConsultationStatus.UNCONFIRMED);
    }

    /**
     * 咨询人已评价
     * @return
     */
    public boolean userEvaluated(){
        return this.userEvaluated.intValue() == 1;
    }

    /**
     * 被咨询人已评价
     * @return
     */
    public boolean byUserEvaluated(){
        return this.byUserEvaluated.intValue() == 1;
    }

    public boolean edited(){
        return cond(ConsultationStatus.EDITED);
    }

    public boolean cond(ConsultationStatus _status){
        return status.equalsIgnoreCase(_status.getCode());
    }

    @JsonIgnore
    public boolean byUser(int userId){
        return this.byUserId.intValue() == userId;
    }

    @JsonIgnore
    public boolean user(int userId){
        return this.userId.intValue() == userId;
    }

    public boolean checkWeek(LocalDate date){
        return date.getDayOfWeek().getValue() == this.week.intValue();
    }

    public boolean checkTime(LocalTime time){
        if (this.code.length() != 8){
            return false;
        }

        LocalTime s = LocalTime.parse(this.code.substring(0, 2) + ":" + this.code.substring(2,4));
        LocalTime e = LocalTime.parse(this.code.substring(4, 6) + ":" + this.code.substring(6,8));
        return time.equals(s) || time.equals(e) || (time.isAfter(s) && time.isBefore(e));
    }
}