package com.blueocean.azbrain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * Created by @author wangzunhui on 2018/3/15.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Creator {
    protected Integer createBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    protected Date createTime;
    protected String deleteFlag;
    protected String remarks;
}
