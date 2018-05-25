package com.blueocean.azbrain.common;

import java.util.Arrays;

/**
 * Created by @author wangzunhui on 2018/3/15.
 */
public enum QuestionStatus {
    EMPTY(""),
    CLOSED("09"),
    NORMAL("00"),
    DELETED("99");

    private String code;

    public String getCode() {
        return code;
    }

    QuestionStatus(String code){
        this.code = code;
    }

    public static QuestionStatus get(String code){
        return Arrays.stream(QuestionStatus.values())
                .filter(u -> u.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(QuestionStatus.EMPTY);
    }
}
