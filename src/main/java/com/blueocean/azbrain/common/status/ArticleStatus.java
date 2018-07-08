package com.blueocean.azbrain.common.status;

import java.util.Arrays;

/**
 * Created by @author wangzunhui on 2018/3/15.
 */
public enum ArticleStatus {
    EMPTY(""),
    NORMAL("00"),
    DRAFT("09"),
    DELETED("99");

    private String code;

    public String getCode() {
        return code;
    }

    ArticleStatus(String code){
        this.code = code;
    }

    public static ArticleStatus get(String code){
        return Arrays.stream(ArticleStatus.values())
                .filter(u -> u.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(ArticleStatus.EMPTY);
    }
}
