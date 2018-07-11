package com.blueocean.azbrain.vo;

import lombok.Data;

import java.util.List;

@Data
public class SpecialistVo {
    // 标签
    private List<String> label;

    // 差评0
    private String bad;

    // 爽约0
    private String contract;

    // 周
    //private Integer week;

    // 时间段代码
    //private String timeline;

    // 可咨询时长
    //private Integer duration;

    private String getLabel(){
        StringBuilder labels = new StringBuilder();
        boolean start = true;
        for (String l : label){
            if (start) {
                labels.append("'");
                start = false;
            } else {
                labels.append(",'");
            }
            labels.append(l);
            labels.append("'");
        }
        return labels.toString();
    }
    // 咨询方式
    private String way;

    private Integer page;
}
