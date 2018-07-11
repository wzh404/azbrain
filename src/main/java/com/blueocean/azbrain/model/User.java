package com.blueocean.azbrain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private Integer id;

    private String name;

    private String pinyin;

    @JsonIgnore
    private String password;

    private String jobNumber;

    private String userType;

    private String photo;

    private String email;

    private String mobile;

    private String businessUnit;

    private String gender;

    private String remarks;

    /** 点赞文章数 */
    private Integer likesNum;

    /** 收藏文章数 */
    private Integer favoriteNum;

    /** 差评数 */
    private Integer badNum;

    /** 中评数 */
    private Integer neutralNum;

    /** 好评数 */
    private Integer goodNum;

    /** 被咨询次数 */
    private Integer consultedNum;

    /** 咨询次数 */
    private Integer consultNum;

    /** 被咨询时长 */
    private Integer consultedDuration;

    /** 咨询时长 */
    private Integer consultDuration;

    /** 爽约数 */
    private Integer breakContractNum;

    /** 一次最大可预约咨询时长 */
    private Integer onceConsultationDuration;

    /** 专家标签 */
    private String label;

    /** 咨询总评价 */
    private BigDecimal consultStar;

    /** 被咨询总评价 */
    private BigDecimal consultedStar;

    private Integer createBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    private String status;

    private Integer loginFlag;
    private Integer messageFlag;
}