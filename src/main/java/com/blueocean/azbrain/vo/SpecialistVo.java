package com.blueocean.azbrain.vo;

import lombok.Data;

@Data
public class SpecialistVo {
    // 标签
    private String label;

    // 差评0
    private String bad;

    // 爽约0
    private String contract;

    // 周
    private Integer week;

    // 时间段代码
    private String timeline;

    // 可咨询时长
    private Integer duration;

    // 咨询方式
    private String way;

    private Integer page;
}
