package com.blueocean.azbrain.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by @author wangzunhui on 2018/3/15.
 */
@Data
public class Creator {
    protected Integer createBy;
    protected Date createTime;
    protected String deleteFlag;
    protected String remarks;
}
