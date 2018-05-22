package com.blueocean.azbrain.common;

import java.util.Arrays;

/**
 * Created by @author wangzunhui on 2018/3/15.
 */
public enum InviteCodeStatus {
    NORMAL("01"),
    RECEIVED("09");

    private String code;

    public String getCode() {
        return code;
    }

    InviteCodeStatus(String code){
        this.code = code;
    }
}
