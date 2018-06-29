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

    private String loginName;

    @JsonIgnore
    private String password;

    private String jobNumber;

    private String userType;

    private String photo;

    private String email;

    private String mobile;

    private String remarks;

    /** 点赞数 */
    private Integer likesNum;

    /** 收藏数 */
    private Integer favoriteNum;

    /** 差评数 */
    private Integer badNum;

    /** 好评数 */
    private Integer goodNum;

    /** 已咨询数 */
    private Integer consultedNum;

    /** 已咨询时长 */
    private Integer consultedDuration;

    /** 爽约数 */
    private Integer breakContractNum;

    /** 可预约咨询时长 */
    private Integer consultationDuration;

    private String label;

    private BigDecimal star;

    private Integer createBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    private String status;
}