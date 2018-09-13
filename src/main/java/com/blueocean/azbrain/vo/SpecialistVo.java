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

    // 驻留时间
    private Integer duration;

    // 查询标签描述
    private List<String> tag;

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
