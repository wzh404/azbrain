package com.blueocean.azbrain.common;

import java.util.Arrays;

/**
 * Created by @author wangzunhui on 2018/3/15.
 */
public enum UserStatus {
    EMPTY(""),
    NORMAL("00"),
    CLOSED("09"),
    DELETED("99");

    private String code;

    public String getCode() {
        return code;
    }

    UserStatus(String code){
        this.code = code;
    }

    public static UserStatus get(String code){
        return Arrays.stream(UserStatus.values())
                .filter(u -> u.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(UserStatus.EMPTY);
    }
}
