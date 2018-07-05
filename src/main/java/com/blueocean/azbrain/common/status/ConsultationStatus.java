package com.blueocean.azbrain.common.status;

import java.util.Arrays;

/**
 * Created by @author wangzunhui on 2018/3/15.
 */
public enum ConsultationStatus {
    EMPTY(""),
    UNCONFIRMED("00"),
    EDITED("01"),
    CONFIRMED("02"),
    UNEVALUATED("03"),
    COMPLETED("10"),
    EXPIRED("97"),
    REJECTED("98"),
    CANCELED("99");

    private String code;

    public String getCode() {
        return code;
    }

    ConsultationStatus(String code){
        this.code = code;
    }

    public static ConsultationStatus get(String code){
        return Arrays.stream(ConsultationStatus.values())
                .filter(u -> u.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(ConsultationStatus.EMPTY);
    }
}
