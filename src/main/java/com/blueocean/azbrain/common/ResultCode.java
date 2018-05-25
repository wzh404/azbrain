package com.blueocean.azbrain.common;

/**
 * Created by @author wangzunhui on 2018/3/15.
 */
public enum ResultCode {
    OK("OK"),
    USER_LOGIN_FAILED("010001"),
    USER_ADD_FAILED("010002"),
    USER_ACCESS_TOKEN("010009"),
    USER_QUESTION_FOLLOWED("020001"),
    USER_ANSWER_LIKED("030001"),
    MANAGE_CLOSED_QUESTION_FAILED("090001");

    private String code;

    public String getCode() {
        return code;
    }

    ResultCode(String code){
        this.code = code;
    }
}
